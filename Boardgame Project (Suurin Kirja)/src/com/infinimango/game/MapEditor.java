package com.infinimango.game;

import java.awt.Graphics;
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
			selectedHex = null;
			for (Hex hex : hexagons) {
				hex.setSelected(false);
				if (hex.getHover()) {
					hex.setSelected(true);
					selectedHex = hex;
				}
			}
			mouseLeft = true;
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

		if (selectedHex != null) {

		}
	}
}
