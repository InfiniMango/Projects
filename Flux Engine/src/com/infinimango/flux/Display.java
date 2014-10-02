package com.infinimango.flux;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import com.infinimango.flux.input.Keyboard;
import com.infinimango.flux.input.Mouse;
import com.infinimango.flux.input.WindowHandler;

public class Display implements Runnable {
	private static final double FLUX_VERSION = 3.0;
	private static boolean MOTDShown = false;

	public static final int UNLIMITED = -1;

	private static JFrame frame;
	private static Canvas canvas;
	private Game game;

	private BufferedImage scaleBuffer;

	private static int width = 640;
	private static int height = 480;
	private static int scale = 1;
	private boolean fullscreen = false;

	private String title = "Flux Game";

	private int targetUPS = 60;
	private int targetFPS = 60;
	private boolean limitUPS = true;
	private boolean limitFPS = false;

	private boolean autoSleep = false;

	private static boolean running;

	private boolean showFPS;

	Thread thread;

	public Display(Game game) {
		this.game = game;
		if (!MOTDShown) {
			MOTDShown = true;
			Debug.print("========== FLUX v." + FLUX_VERSION
					+ "RUNNING ==========");
		}
	}

	public void setSize(int width, int height) {
		Display.width = width;
		Display.height = height;
	}

	public void setFullscreen(boolean fullscreen) {
		this.fullscreen = fullscreen;
	}

	public void setScaling(int scale) {
		Display.scale = scale;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setTargetUPS(int targetUPS) {
		if (targetUPS == UNLIMITED) {
			limitUPS = false;
			return;
		} else {
			limitUPS = true;
		}
		this.targetUPS = targetUPS;
	}

	public void setTargetFPS(int targetFPS) {
		if (targetFPS == UNLIMITED) {
			limitFPS = false;
			return;
		} else {
			limitFPS = true;
		}
		this.targetFPS = targetFPS;
	}

	public void setAutoSleep(boolean autoSleep) {
		this.autoSleep = autoSleep;
	}

	public void showFPS(boolean showFPS) {
		this.showFPS = showFPS;
	}

	public void create() {
		long timer = System.currentTimeMillis();
		Debug.out("Creating display...");
		frame = new JFrame(title);
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		frame.setResizable(false);

		Dimension size = new Dimension(width * scale, height * scale);
		if (fullscreen) {
			size = new Dimension(Toolkit.getDefaultToolkit().getScreenSize());
			width = (int) Toolkit.getDefaultToolkit().getScreenSize()
					.getWidth()
					/ scale;
			height = (int) Toolkit.getDefaultToolkit().getScreenSize()
					.getHeight()
					/ scale;
			Debug.out("Set display to fullscreen mode, " + width + "x" + height
					+ " at a scale of " + scale);
		}
		canvas = new Canvas();
		canvas.setMinimumSize(size);
		canvas.setMaximumSize(size);
		canvas.setPreferredSize(size);

		canvas.addKeyListener(new Keyboard());
		canvas.addMouseListener(new Mouse(scale));
		canvas.addMouseMotionListener(new Mouse(scale));
		canvas.addMouseWheelListener(new Mouse(scale));

		frame.addWindowListener(new WindowHandler());

		if (fullscreen) {
			GraphicsDevice device = GraphicsEnvironment
					.getLocalGraphicsEnvironment().getDefaultScreenDevice();
			frame.setUndecorated(true);
			frame.setAlwaysOnTop(true);
			device.setFullScreenWindow(frame);
		}

		frame.add(canvas);
		frame.pack();

		if (!fullscreen)
			frame.setLocationRelativeTo(null);

		frame.setVisible(true);

		canvas.requestFocus();
		canvas.setIgnoreRepaint(true);

		Debug.out("Display created in " + (System.currentTimeMillis() - timer)
				+ "ms!");

		start();
	}

	private synchronized void start() {
		Debug.out("Starting thread");
		Display.running = true;
		thread = new Thread(this, frame.getTitle() + " - main");
		thread.start();
	}

	private synchronized void stop() {
		Debug.out("Stopping thread");
		Display.running = false;
		frame.dispose();
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		long last = System.nanoTime();
		long now;

		int frames = 0;
		int updates = 0;

		long timer = System.currentTimeMillis();

		double deltaUPS = 0;
		double deltaFPS = 0;

		while (running) {
			game.updateClock();

			now = System.nanoTime();

			if (limitUPS)
				deltaUPS += (now - last)
						/ (1000.0 * 1000.0 * 1000.0 / targetUPS);
			if (limitFPS)
				deltaFPS += (now - last)
						/ (1000.0 * 1000.0 * 1000.0 / targetFPS);

			last = now;

			if (!limitUPS || deltaUPS >= 1) {
				game.update();
				updates++;
				deltaUPS--;
				if (targetUPS == targetFPS)
					deltaFPS = 1;
			}

			if (!limitFPS || deltaFPS >= 1) {
				render();
				frames++;
				deltaFPS--;
			} else {
				if (autoSleep)
					sleep();
			}

			if (System.currentTimeMillis() - timer >= 1000) {
				timer = System.currentTimeMillis();

				String msg = updates + " ups - " + frames + " fps";
				Debug.print(msg);

				if (showFPS)
					frame.setTitle(title + " - " + msg);

				updates = 0;
				frames = 0;
			}
		}
		stop();
	}

	public static void close() {
		running = false;
	}

	protected void render() {
		BufferStrategy bufferStrategy = canvas.getBufferStrategy();
		if (bufferStrategy == null) {
			canvas.createBufferStrategy(3);
			return;
		}
		if (scaleBuffer == null) {
			scaleBuffer = new BufferedImage(width, height,
					BufferedImage.TYPE_INT_RGB);
		}

		do {
			do {
				Graphics2D rawGraphics = (Graphics2D) bufferStrategy
						.getDrawGraphics();

				Graphics2D g = scaleBuffer.createGraphics();

				g.setColor(Color.BLACK);
				g.fillRect(0, 0, width, height);

				game.render(g);
				rawGraphics.drawImage(scaleBuffer, 0, 0, width * scale, height
						* scale, null);

				g.dispose();
				rawGraphics.dispose();
			} while (bufferStrategy.contentsRestored());

			bufferStrategy.show();
		} while (bufferStrategy.contentsLost());
	}

	public static void sleep() {
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static int getWidth() {
		return width;
	}

	public static int getScaledWidth() {
		return width * scale;
	}

	public static int getHeight() {
		return height;
	}

	public static int getScaledHeight() {
		return height * scale;
	}

	public static int getScale() {
		return scale;
	}

	public static boolean hasFocus() {
		return canvas.hasFocus();
	}

	public static void setIcon(BufferedImage icon) {
		frame.setIconImage(icon);
	}

	public static void setCursor(BufferedImage cursorImage) {
		Cursor cursor = Toolkit.getDefaultToolkit().createCustomCursor(
				cursorImage, new Point(0, 0), "Flux - Custom cursor");
		frame.setCursor(cursor);
	}

	public static boolean isRunning() {
		return running;
	}

	public static void setLocation(int x, int y) {
		frame.setLocation(x, y);
	}

	public static void resetLocation() {
		frame.setLocationRelativeTo(null);
	}
}
