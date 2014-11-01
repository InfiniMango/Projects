package com.infinimango.game;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import com.infinimango.flux.Resource;
import com.infinimango.flux.State;
import com.infinimango.flux.input.Keyboard;
import com.infinimango.flux.input.Mouse;
import com.infinimango.flux.world.Camera;

public class MapEditor extends State {
	public static final int H_SPACING = 24;
	public static final int V_SPACING = 20;

	HexList hexagons = new HexList();

	Hex flashes[] = new Hex[6];

	int mouseW = Mouse.getWheelRotation();

	boolean mouseLeft;
	boolean q;

	boolean saving;
	boolean loading;

	public static final BufferedImage hexFlash = Resource
			.loadImage("res/hex_w.png");

	Hex selectedHex;

	public MapEditor() {
		Hex firstHex = new Hex(0, 0, Hex.RED, true, new Point3D(0, 0, 0));
		hexagons.add(firstHex);
		Camera.centerOn(firstHex);
	}

	@Override
	public void update() {
		if (Keyboard.isKeyDown(KeyEvent.VK_ESCAPE)) {
			Game.quit();
		}

		// INPUT COMMANDS:

		// MOUSE WHEEL - SHIFT COLORS
		int mouseWDelta = Mouse.getWheelRotation() - mouseW;
		mouseW = Mouse.getWheelRotation();

		if (mouseWDelta != 0) {
			for (Hex hex : hexagons) {
				hex.shiftColor(mouseWDelta);
			}
		}

		// MOUSE LEFT - ADD OR SELECT HEX
		if (!mouseLeft && Mouse.buttonDown(Mouse.LEFT)) {
			boolean hovered = false;
			for (Hex hex : hexagons) {
				hex.setSelected(false);
				if (hex.getHover()) {
					hovered = true;
					hex.setSelected(true);
					selectedHex = hex;

					Hex sh = selectedHex;
					int hx = sh.getLocation().getX();
					int hy = sh.getLocation().getY();
					int hz = sh.getLocation().getZ();

					// LEFT - TOP
					flashes[0] = new Hex(sh.getX() - sh.getWidth() / 3 * 2 - 7,
							sh.getY() - sh.getHeight() / 2, hexFlash,
							new Point3D(hx - 1, hy + 1, hz));
					// LEFT - BOT
					flashes[1] = new Hex(sh.getX() - sh.getWidth() / 3 * 2 - 7,
							sh.getY() + sh.getHeight() / 2, hexFlash,
							new Point3D(hx - 1, hy, hz + 1));
					// TOP
					flashes[2] = new Hex(sh.getX(), sh.getY() - sh.getHeight(),
							hexFlash, new Point3D(hx, hy + 1, hz - 1));
					// BOT
					flashes[3] = new Hex(sh.getX(), sh.getY() + sh.getHeight(),
							hexFlash, new Point3D(hx, hy - 1, hz + 1));
					// RIGHT - TOP
					flashes[4] = new Hex(sh.getX() + sh.getWidth() / 3 * 2 + 7,
							sh.getY() - sh.getHeight() / 2, hexFlash,
							new Point3D(hx + 1, hy, hz - 1));
					// RIGHT - BOT
					flashes[5] = new Hex(sh.getX() + sh.getWidth() / 3 * 2 + 7,
							sh.getY() + sh.getHeight() / 2, hexFlash,
							new Point3D(hx + 1, hy - 1, hz));
					break;
				}
			}

			for (int i = 0; i < 6; i++) {
				if (flashes[i] != null && flashes[i].getHover()) {
					hovered = true;
					int col = 0;
					Hex sh = selectedHex;
					int xd = flashes[i].getLocation().getX()
							- sh.getLocation().getX();
					int yd = flashes[i].getLocation().getY()
							- sh.getLocation().getY();

					if (Math.abs(xd) == 1)
						col = 1;
					if (Math.abs(yd) == 1)
						col = 2;

					hexagons.add(new Hex(flashes[i].getX(), flashes[i].getY(),
							col, false, flashes[i].getLocation()));
					break;
				}
			}
			mouseLeft = true;
			if (!hovered)
				selectedHex = null;
		}

		if (!Mouse.buttonDown(Mouse.LEFT))
			mouseLeft = false;

		// Q - TOGGLE SPECIAL
		if (!q && Keyboard.isKeyDown(KeyEvent.VK_Q)) {

			q = true;
		}

		if (!Keyboard.isKeyDown(KeyEvent.VK_Q))
			q = false;

		// S - SAVE
		if (!saving && Keyboard.isKeyDown(KeyEvent.VK_S)) {
			saving = true;
			save();
		}

		if (!Keyboard.isKeyDown(KeyEvent.VK_S))
			saving = false;

		// L - LOAD
		if (!loading && Keyboard.isKeyDown(KeyEvent.VK_L)) {
			loading = true;
			load();
		}

		if (!Keyboard.isKeyDown(KeyEvent.VK_L))
			loading = false;

		// W, A, S, D - CAMERA MOVEMENT
		if (Keyboard.isKeyDown(KeyEvent.VK_W))
			Camera.moveUp(2.5f);
		if (Keyboard.isKeyDown(KeyEvent.VK_S))
			Camera.moveDown(2.5f);
		if (Keyboard.isKeyDown(KeyEvent.VK_A))
			Camera.moveLeft(2.5f);
		if (Keyboard.isKeyDown(KeyEvent.VK_D))
			Camera.moveRight(2.5f);
	}

	private void save() {
		Calendar c = Calendar.getInstance();
		String fileName = "map_";
		fileName += c.get(Calendar.DATE);
		fileName += "-" + c.get(Calendar.MONTH);
		fileName += "-" + c.get(Calendar.YEAR);
		fileName += "_" + c.get(Calendar.HOUR);
		fileName += "-" + c.get(Calendar.MINUTE);
		fileName += "-" + c.get(Calendar.SECOND);

		File file = new File(fileName);

		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		FileOutputStream s = null;
		try {
			s = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// for (int y = 0; y < HEIGHT; y++) {
		// for (int x = 0; x < WIDTH; x++) {
		// try {
		// s.write(data[x][y]);
		// s.write(special[x][y] ? 1 : 0);
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// }
		// }

		try {
			s.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("SAVED " + fileName);
	}

	private void load() {
		File file = new File("map_load");

		FileInputStream s = null;
		try {
			s = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		// for (int y = 0; y < HEIGHT; y++) {
		// for (int x = 0; x < WIDTH; x++) {
		// try {
		// data[x][y] = s.read();
		// special[x][y] = s.read() == 1;
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// }
		// }

		try {
			s.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("LOADED");
	}

	@Override
	public void render(Graphics g) {
		hexagons.render(g);

		for (Hex hex : hexagons) {
			if (hex.getHover()) {
				drawOpaqueImg(hexFlash, hex.getScreenX(), hex.getScreenY(),
						0.25f, g);
				break;
			}
		}

		if (selectedHex != null) {
			setOpacity(g, 0.5f);
			for (int i = 0; i < 6; i++) {
				if (flashes[i] != null)
					flashes[i].render(g);
				if (flashes[i].getHover()) {
					g.drawImage(hexFlash, flashes[i].getScreenX(),
							flashes[i].getScreenY(), null);
				}
			}
			setOpacity(g, 1f);
		}

		g.setColor(Color.white);
		g.drawString("Hexagons: " + hexagons.size(), 0, 14);

	}

	private void setOpacity(Graphics g, float opacity) {
		((Graphics2D) g).setComposite(AlphaComposite.getInstance(
				AlphaComposite.SRC_OVER, opacity));
	}

	private void drawOpaqueImg(BufferedImage img, int x, int y, float opacity,
			Graphics g2) {
		Graphics2D g = (Graphics2D) g2;
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
				opacity));
		g.drawImage(img, x, y, null);
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
	}
}
