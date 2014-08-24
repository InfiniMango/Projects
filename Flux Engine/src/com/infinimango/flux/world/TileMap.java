package com.infinimango.flux.world;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import com.infinimango.flux.world.tile.AnimatedTile;
import com.infinimango.flux.world.tile.Tile;

public class TileMap {
	Tile tile[][];
	int width, height;
	int spacing;

	public TileMap(BufferedImage mapImage, HashMap<Color, Tile> tileType,
			int spacing) {
		width = mapImage.getWidth();
		height = mapImage.getHeight();
		tile = new Tile[width][height];
		this.spacing = spacing;

		// Create every tile according to color-tile HashMap
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				try {
					tile[x][y] = tileType.get(new Color(mapImage.getRGB(x, y)))
							.getClass().newInstance();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public BufferedImage getMiniMap(HashMap<Tile, Color> tileColors) {
		BufferedImage miniMap = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				miniMap.setRGB(x, y, tileColors.get(tile[x][y]).getRGB());
			}
		}
		return miniMap;
	}

	public void update() {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (tile[x][y] instanceof AnimatedTile) {
					((AnimatedTile) tile[x][y]).update();
				}
			}
		}
	}

	public void render(Graphics g) {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				tile[x][y].render(Camera.getX() - x * spacing, Camera.getY()
						- y * spacing, g);
			}
		}
	}

	public boolean collidesAt(int x, int y) {
		return tile[x / spacing][y / spacing].collides();
	}

	public boolean collidesAt(Point point) {
		return collidesAt((int) point.getX(), (int) point.getY());
	}

	public int getWidth() {
		return width;
	}

	public int getWidthInPixels() {
		return width * spacing;
	}

	public int getHeight() {
		return height;
	}

	public int getHeightInPixels() {
		return height * spacing;
	}

	public int getSpacing() {
		return spacing;
	}

	public void setSpacing(int spacing) {
		this.spacing = spacing;
	}
}
