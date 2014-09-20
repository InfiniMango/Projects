package com.infinimango.game;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;

import com.infinimango.flux.Resource;
import com.infinimango.flux.world.Camera;

public class HexaMap {
	int width, height;

	int data[][];
	boolean special[][];

	int hSpacing, vSpacing;

	BufferedImage[] hexImg = { null, Resource.loadImage("res/hex_r.png"),
			Resource.loadImage("res/hex_b.png"),
			Resource.loadImage("res/hex_g.png") };

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
	}
}
