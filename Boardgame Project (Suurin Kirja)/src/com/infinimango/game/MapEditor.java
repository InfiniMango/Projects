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
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
	boolean mouseRight;
	boolean q;

	boolean saving;
	boolean loading;
	
	boolean setMode;

	public static final BufferedImage hexFlash = Resource
			.loadImage("res/hex_w.png");

	Hex selectedHex;
	boolean pathSelect;

	public MapEditor() {
		Hex firstHex = new Hex(0, 0, Hex.RED, false, new Point3D(0, 0, 0));
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
		if ((!mouseLeft && Mouse.buttonDown(Mouse.LEFT)) || pathSelect) {
			pathSelect = false;
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
					// RIGHT - TOP
					flashes[1] = new Hex(sh.getX() + sh.getWidth() / 3 * 2 + 7,
							sh.getY() - sh.getHeight() / 2, hexFlash,
							new Point3D(hx + 1, hy, hz - 1));
					// BOT
					flashes[2] = new Hex(sh.getX(), sh.getY() + sh.getHeight(),
							hexFlash, new Point3D(hx, hy - 1, hz + 1));
					// LEFT - BOT
					flashes[3] = new Hex(sh.getX() - sh.getWidth() / 3 * 2 - 7,
							sh.getY() + sh.getHeight() / 2, hexFlash,
							new Point3D(hx - 1, hy, hz + 1));
					// RIGHT - BOT
					flashes[4] = new Hex(sh.getX() + sh.getWidth() / 3 * 2 + 7,
							sh.getY() + sh.getHeight() / 2, hexFlash,
							new Point3D(hx + 1, hy - 1, hz));
					// TOP
					flashes[5] = new Hex(sh.getX(), sh.getY() - sh.getHeight(),
							hexFlash, new Point3D(hx, hy + 1, hz - 1));
					break;
				}
			}

			for (int i = 0; i < 6; i++) {
				if (flashes[i] != null && flashes[i].getHover()) {
					hovered = true;
					Hex sh = selectedHex;
					if(sh == null) break;
					int col = sh.color;
//					int xd = flashes[i].getLocation().getX()
//							- sh.getLocation().getX();
//					int yd = flashes[i].getLocation().getY()
//							- sh.getLocation().getY();
//					int zd = flashes[i].getLocation().getZ()
//							- sh.getLocation().getZ();
//					
////					if(xd == 0 && yd != 0 && zd != 0) col += 0;
////					if(yd == 0 && xd != 0 && zd != 0) col += yd;
////					if(xd == 0 && yd != 0 && zd == 0) col += 1;
////					if(xd != 0 && yd == 0 && zd == 0) col += 1;
//					
					col += i < 3? 1 : 2;
					
					col %= 3;
					
//					if(col < 0) col = 2;
//					if(col > 2) col = 0;
					
					hexagons.add(new Hex(flashes[i].getX(), flashes[i].getY(),
							col, false, flashes[i].getLocation()));
					pathSelect = setMode;
					break;
				}
			}
			mouseLeft = true;
			if (!hovered)
				selectedHex = null;
		}

		if (!Mouse.buttonDown(Mouse.LEFT))
			mouseLeft = false;

		// Q - TOGGLE AUTOSET
		if (!q && Keyboard.isKeyDown(KeyEvent.VK_Q)) {
			setMode = !setMode;
			q = true;
		}

		if (!Keyboard.isKeyDown(KeyEvent.VK_Q))
			q = false;
		
		// DELETE - DELETE (duh)
		if(Keyboard.isKeyDown(KeyEvent.VK_DELETE)){
			if(selectedHex != null) {
				Point3D location = selectedHex.getLocation();
				selectedHex = null;
				
				for(int i = 0; i < hexagons.size(); i++){
					if(hexagons.get(i).getLocation().matches(location)){
						if(location.matches(new Point3D(0, 0, 0))) return;
						hexagons.remove(i);
						return;
					}
				}
			}
		}
		
		// MOUSE RIGHT / MIDDLE - PLACE SPECIAL
		if (!mouseRight && Mouse.buttonDown(Mouse.MIDDLE)) {
			for(Hex hex : hexagons){
				if(hex.getHover()){
					hex.toggleSpecial();
					break;
				}
			}
			mouseRight = true;
		}

		if (!Mouse.buttonDown(Mouse.MIDDLE))
			mouseRight = false;

		// S - SAVE
		if (!saving && Keyboard.isKeyDown(KeyEvent.VK_F1)) {
			saving = true;
			try {
				save();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (!Keyboard.isKeyDown(KeyEvent.VK_F1))
			saving = false;

		// L - LOAD
		if (!loading && Keyboard.isKeyDown(KeyEvent.VK_F2)) {
			loading = true;
			try {
				load();
			} catch (Exception e){
				e.printStackTrace();
			}
		}

		if (!Keyboard.isKeyDown(KeyEvent.VK_F2))
			loading = false;

		// W, A, S, D - CAMERA MOVEMENT
		if (Keyboard.isKeyDown(KeyEvent.VK_W))
			Camera.moveUp(5);
		if (Keyboard.isKeyDown(KeyEvent.VK_S))
			Camera.moveDown(5);
		if (Keyboard.isKeyDown(KeyEvent.VK_A))
			Camera.moveLeft(5);
		if (Keyboard.isKeyDown(KeyEvent.VK_D))
			Camera.moveRight(5);
	}

	private void save() throws IOException {
		Calendar c = Calendar.getInstance();
		String fileName = "newmap_";
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
				e.printStackTrace();
			}
		}

		ObjectOutputStream o = null;
		try {
			FileOutputStream s = new FileOutputStream(file);
			o = new ObjectOutputStream(s);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		o.writeInt(hexagons.size());
		
		for(Hex hex : hexagons){
			o.writeInt(hex.getX());
			o.writeInt(hex.getY());
			
			o.writeInt(hex.getLocation().getX());
			o.writeInt(hex.getLocation().getY());
			o.writeInt(hex.getLocation().getZ());
			
			o.writeInt(hex.getColor());
			o.writeBoolean(hex.isSpecial());
//			o.writeObject(hex);
		}

		o.close();

		System.out.println("SAVED " + fileName);
	}

	private void load() throws IOException, ClassNotFoundException {
		File file = new File("newmap_load");

		ObjectInputStream o = null;
		try {
			FileInputStream s = new FileInputStream(file);
			o = new ObjectInputStream(s);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		int amount = o.readInt();
		
		hexagons.clear();
		
		for(int i = 0; i < amount; i++){
			int x = o.readInt();
			int y = o.readInt();
			
			int x2 = o.readInt();
			int y2 = o.readInt();
			int z2 = o.readInt();
			
			int color = o.readInt();
			boolean special = o.readBoolean();
			hexagons.add(new Hex(x, y, color, special, new Point3D(x2, y2, z2)));
//			hexagons.add((Hex)o.readObject());
		}

		o.close();

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
		g.drawString("WASD to move the camera, MOUSE LEFT to select and place hexes, MOUSE WHEEL to change color, DELETE to - you know - delete hexes", 0, 14);
		g.drawString("Q to change tile placement mode - current mode: " + (setMode? "PATH" : "CIRCLE"), 0, 28);
		g.drawString("Hexagons: " + hexagons.size(), 0, 42);

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
