package com.infinimango.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import com.infinimango.flux.Display;
import com.infinimango.flux.State;
import com.infinimango.flux.input.Keyboard;
import com.infinimango.flux.input.Mouse;

public class MapEditor extends State {

	public static final int WIDTH = 22;
	public static final int HEIGHT = 13;

	public static final int H_SPACING = 32;
	public static final int V_SPACING = 24;

	public int[][] data = new int[WIDTH][HEIGHT];
	public boolean[][] special = new boolean[WIDTH][HEIGHT];

	int currentId = 0;
	final int maxId = 3;

	int mouseW = Mouse.getWheelRotation();

	Color[] colors = { null, new Color(253, 207, 159),
			new Color(142, 214, 246), new Color(165, 206, 68) };

	boolean mouseLeft;
	boolean mouseRight;

	boolean saving;
	boolean loading;

	public MapEditor() {
	}

	@Override
	public void update() {
		int mouseWDelta = Mouse.getWheelRotation() - mouseW;

		currentId += mouseWDelta;
		mouseW = Mouse.getWheelRotation();

		if (currentId > maxId)
			currentId = 0;

		if (currentId < 0)
			currentId = maxId;

		if (!mouseLeft && Mouse.buttonDown(Mouse.LEFT)) {
			data[Mouse.getX() / (H_SPACING)][Mouse.getY() / (V_SPACING)] = currentId;
			mouseLeft = true;
		}

		if (!mouseRight && Mouse.buttonDown(Mouse.RIGHT)) {
			special[Mouse.getX() / (H_SPACING)][Mouse.getY() / (V_SPACING)] = !special[Mouse
					.getX() / (H_SPACING)][Mouse.getY() / (V_SPACING)];
			mouseRight = true;
		}

		if (!Mouse.buttonDown(Mouse.LEFT))
			mouseLeft = false;

		if (!Mouse.buttonDown(Mouse.RIGHT))
			mouseRight = false;

		if (!saving && Keyboard.isKeyDown(KeyEvent.VK_S)) {
			saving = true;
			save();
		}

		if (!Keyboard.isKeyDown(KeyEvent.VK_S))
			saving = false;

		if (!loading && Keyboard.isKeyDown(KeyEvent.VK_L)) {
			loading = true;
			load();
		}

		if (!Keyboard.isKeyDown(KeyEvent.VK_L))
			loading = false;
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

		for (int y = 0; y < HEIGHT; y++) {
			for (int x = 0; x < WIDTH; x++) {
				try {
					s.write(data[x][y]);
					s.write(special[x][y] ? 1 : 0);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

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

		for (int y = 0; y < HEIGHT; y++) {
			for (int x = 0; x < WIDTH; x++) {
				try {
					data[x][y] = s.read();
					special[x][y] = s.read() == 1;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

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
		for (int y = 0; y < HEIGHT; y++) {
			for (int x = 0; x < WIDTH; x++) {
				int yShift = x % 2 == 1 ? V_SPACING / 2 : 0;

				g.setColor(Color.WHITE);
				g.drawRect(x * H_SPACING, y * V_SPACING + yShift, 16, 16);

				if (data[x][y] > 0) {
					g.setColor(colors[data[x][y]]);
					g.fillRect(x * H_SPACING + 1, y * V_SPACING + yShift + 1,
							15, 15);
				}

				if (special[x][y] == true) {
					g.setColor(new Color(128, 64, 0));
					g.fillRect(x * H_SPACING + 4 + 1, y * V_SPACING + yShift
							+ 4 + 1, 7, 7);
				}

				if (currentId > 0) {
					g.setColor(colors[currentId]);
					g.fillRect(16, Display.getHeight() - 40, 24, 24);
				}
			}
		}
	}
}
