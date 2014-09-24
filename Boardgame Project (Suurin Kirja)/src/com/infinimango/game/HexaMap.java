package com.infinimango.game;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;

import com.infinimango.flux.Resource;
import com.infinimango.flux.input.Mouse;
import com.infinimango.flux.world.Camera;

public class HexaMap {
	int width, height;

	int data[][];
	boolean special[][];

	int hSpacing, vSpacing;

	int highlightX, highlightY;

	BufferedImage highlightImg;

	BufferedImage[] hexImg = { null, Resource.loadImage("res/hex_b.png"),
			Resource.loadImage("res/hex_g.png"),
			Resource.loadImage("res/hex_r.png") };

	BufferedImage[] hexImgExl = { null,
			Resource.loadImage("res/hex_b_exl.png"),
			Resource.loadImage("res/hex_g_exl.png"),
			Resource.loadImage("res/hex_r_exl.png") };

	public HexaMap(String path, int width, int height, int hSpacing,
			int vSpacing) {
		this.width = width;
		this.height = height;

		data = new int[width][height];
		special = new boolean[width][height];

		this.hSpacing = hSpacing;
		this.vSpacing = vSpacing;

		highlightImg = Resource.loadImage("res/hex_hl.png");

		load(path);
	}

	private void load(String path) {
		File file = new File(path);

		try {
			FileInputStream s = new FileInputStream(file);

			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {

					data[x][y] = s.read();
					special[x][y] = s.read() == 1;

				}
			}
			s.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void update(int x2, int y2) {
		if (Mouse.getX() > x2 - Camera.getX()
				&& Mouse.getY() > y2 - Camera.getY()) {
			if (Mouse.getX() < x2 + width * hSpacing
					&& Mouse.getY() < y2 + height * vSpacing) {
				LOOP: for (int y = 0; y < height; y++) {
					for (int x = 0; x < width; x++) {

						if (data[x][y] == 0)
							continue;

						int columnShift = 0;
						if (x % 2 == 1)
							columnShift += vSpacing / 2;

						int drawX = x * hSpacing - Camera.getX() + x2;
						int drawY = y * vSpacing + columnShift - Camera.getY()
								+ y2;

						if (Mouse.getX() > drawX && Mouse.getY() > drawY) {
							if (Mouse.getX() < drawX
									+ hexImg[data[x][y]].getWidth()
									&& Mouse.getY() < drawY
											+ hexImg[data[x][y]].getHeight()) {

								int realX = Mouse.getX() - drawX;
								int realY = Mouse.getY() - drawY;
								if (!isOnImage(hexImg[data[x][y]], realX, realY)) {
									System.out.println("this");
									continue;
								}

								highlightX = drawX
										+ hexImg[data[x][y]].getWidth() / 2
										- highlightImg.getWidth() / 2;
								highlightY = drawY
										+ hexImg[data[x][y]].getHeight() / 2
										- highlightImg.getHeight() / 2;
								break LOOP;
							}
						}

					}
					highlightX = -100;
					highlightY = -100;
				}
			}
		}
	}

	private boolean isOnImage(BufferedImage img, int x, int y) {
		return img.getRGB(x, y) >> 24 != 0x00;
	}

	public void render(int x2, int y2, Graphics g) {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (data[x][y] == 0)
					continue;

				int columnShift = 0;
				if (x % 2 == 1)
					columnShift += vSpacing / 2;

				BufferedImage hex = null;

				if (special[x][y]) {
					hex = hexImgExl[data[x][y]];
				} else {
					hex = hexImg[data[x][y]];
				}

				int drawX = x * hSpacing - Camera.getX() + x2;
				int drawY = y * vSpacing + columnShift - Camera.getY() + y2;

				g.drawImage(hex, drawX, drawY, null);

			}
		}
		if (highlightX == -100 && highlightY == -100)
			return;

		g.drawImage(highlightImg, highlightX, highlightY, null);
	}
}
