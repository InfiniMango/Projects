package com.infinimango.game;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;

import com.infinimango.flux.Resource;

public class HexaMap {
	int width, height;
	float hSpacing, vSpacing;

	int highlightX, highlightY;
	int selectedX, selectedY;

	BufferedImage highlightImg;

	BufferedImage[] hexImg = { null, Resource.loadImage("res/hex_b.png"),
			Resource.loadImage("res/hex_g.png"),
			Resource.loadImage("res/hex_r.png") };

	BufferedImage[] hexImgExl = { null,
			Resource.loadImage("res/hex_b_exl.png"),
			Resource.loadImage("res/hex_g_exl.png"),
			Resource.loadImage("res/hex_r_exl.png") };

	int steps = 2;

	Hexagon hexagon[][];

	int x, y;

	public HexaMap(int x, int y, String path, int width, int height,
			float hSpacing, float vSpacing) {
		this.x = x;
		this.y = y;

		this.width = width;
		this.height = height;

		this.hSpacing = hSpacing;
		this.vSpacing = vSpacing;

		highlightImg = Resource.loadImage("res/hex_hl.png");

		hexagon = new Hexagon[width][height];
		load(path);
	}

	private void load(String path) {
		File file = new File(path);

		try {
			FileInputStream s = new FileInputStream(file);

			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					int data = s.read();
					if (data == 0) {
						s.read();
						continue;
					}

					float columnShift = x % 2 == 1 ? vSpacing / 2 : 0;

					hexagon[x][y] = new Hexagon(this.x + x * hSpacing, this.y
							+ y * vSpacing + columnShift, data
							+ (s.read() == 1 ? 3 : 0));

				}
			}
			s.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void clearHighlights() {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (hexagon[x][y] == null)
					continue;
				hexagon[x][y].setHighlight(false);
			}
		}
	}

	public void highlight(int x, int y, int distance) {
		int shift = x % 2 == 1 ? 1 : 0;

		hexagon[x][y].setHighlight(true);

		if (x > 0 && y < height - 1 && hexagon[x - 1][y + shift] != null)
			hexagon[x - 1][y + shift].setHighlight(true);
		if (x > 0 && y > 0 && hexagon[x - 1][y - 1 + shift] != null)
			hexagon[x - 1][y - 1 + shift].setHighlight(true);

		if (x < width - 1 && hexagon[x + 1][y + shift] != null)
			hexagon[x + 1][y + shift].setHighlight(true);
		if (x < width - 1 && y > 0 && hexagon[x + 1][y - 1 + shift] != null)
			hexagon[x + 1][y - 1 + shift].setHighlight(true);

		if (y > 0 && hexagon[x][y - 1] != null)
			hexagon[x][y - 1].setHighlight(true);
		if (y < height - 1 && hexagon[x][y + 1] != null)
			hexagon[x][y + 1].setHighlight(true);
	}

	public void update() {
		clearHighlights();
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (hexagon[x][y] != null && hexagon[x][y].getHover()) {
					highlight(x, y, 0);
					return;
				}
			}
		}
	}

	public void render(int x2, int y2, Graphics g) {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (hexagon[x][y] != null)
					hexagon[x][y].render(g);
			}
		}
	}
}
