package com.infinimango.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import com.infinimango.flux.Display;
import com.infinimango.flux.Resource;
import com.infinimango.flux.State;
import com.infinimango.flux.input.Keyboard;
import com.infinimango.flux.world.Camera;

public class GameState extends State {
	String str;
	Reads r = new Reads();

	HexaMap map;

	int cameraSpeed = 4;

	boolean guiToggle = true;
	boolean togglePressed;

	BufferedImage background;

	//
	// int tx, ty;

	public GameState() {
		str = r.getContent();

		background = Resource.loadImage("res/board.png");
		map = new HexaMap("map_default", 22, 13, 48, 57);
	}

	@Override
	public void update() {
		// Camera movement
		if (Keyboard.isKeyDown(KeyEvent.VK_W))
			Camera.moveUp(cameraSpeed);
		if (Keyboard.isKeyDown(KeyEvent.VK_S))
			Camera.moveDown(cameraSpeed);
		if (Keyboard.isKeyDown(KeyEvent.VK_A))
			Camera.moveLeft(cameraSpeed);
		if (Keyboard.isKeyDown(KeyEvent.VK_D))
			Camera.moveRight(cameraSpeed);

		if (!togglePressed && Keyboard.isKeyDown(KeyEvent.VK_G)) {
			guiToggle = !guiToggle;
			togglePressed = true;
		}

		if (!Keyboard.isKeyDown(KeyEvent.VK_G))
			togglePressed = false;

		// if (Keyboard.isKeyDown(KeyEvent.VK_LEFT))
		// tx--;
		// if (Keyboard.isKeyDown(KeyEvent.VK_UP))
		// ty--;
		// if (Keyboard.isKeyDown(KeyEvent.VK_RIGHT))
		// tx++;
		// if (Keyboard.isKeyDown(KeyEvent.VK_DOWN))
		// ty++;
		//
		// if (Keyboard.isKeyDown(KeyEvent.VK_T))
		// System.out.println("x: " + tx + " - y: " + ty);

	}

	@Override
	public void render(Graphics g) {
		g.drawImage(background, -Camera.getX(), -Camera.getY(), null);
		// map.render(tx, ty, g);
		map.render(92, 49, g);

		if (guiToggle) {
			g.setColor(new Color(108, 72, 32));
			int w = Display.getWidth();
			int h = Display.getHeight();
			g.fillRect(w - w / 4, 0, w / 2, h);
			g.fillRect(0, h - h / 4, w, h / 4);
		}

		g.setColor(Color.white);
		g.drawString(str, 20, 30);
	}
}
