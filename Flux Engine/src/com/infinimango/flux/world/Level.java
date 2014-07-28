package com.infinimango.flux.world;

import com.infinimango.flux.world.tile.AnimatedTile;
import com.infinimango.flux.world.tile.Tile;

import java.awt.*;

public class Level {
	private int x, y, width, height, tileWidth, tileHeight;
	private Tile tiles[][];

	public Level(int x, int y, int width, int height, int tileWidth, int tileHeight, Tile tiles[][]) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
		this.tiles = tiles;
	}

	public Level(int width, int height, int tileWidth, int tileHeight, Tile tiles[][]) {
		this(0, 0, width, height, tileWidth, tileHeight, tiles);
	}

	public Tile getTile(int x, int y) {
		return tiles[x][y];
	}

	public void setTile(int x, int y, Tile tile) {
		tiles[x][y] = tile;
	}

	public void setTiles(Tile tiles[][]) {
		this.tiles = tiles;
	}

	public Tile[][] getTiles() {
		return tiles;
	}

	public void setLocation(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void setLocation(Point p) {
		setLocation((int) p.getX(), (int) p.getY());
	}

	public Point getLocation() {
		return new Point(x, y);
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getScreenX() {
		return x - Camera.getX();
	}

	public int getX() {
		return x;
	}

	public int getScreenY() {
		return y - Camera.getY();
	}

	public int getY() {
		return y;
	}

	public int getTileWidth() {
		return tileWidth;
	}

	public int getTileHeight() {
		return tileHeight;
	}

	public void setTileWidth(int tileWidth) {
		this.tileWidth = tileWidth;
	}

	public void setTileHeight(int tileHeight) {
		this.tileHeight = tileHeight;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getWidthInPixels() {
		return width * tileWidth;
	}

	public int getHeightInPixels() {
		return height * tileHeight;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public boolean collides(int x, int y) {
		return getTile(x, y).collides();
	}

	public void update() {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (tiles[x][y] instanceof AnimatedTile) ((AnimatedTile) tiles[x][y]).update();
			}
		}
	}

	public void render(Graphics g) {
		int startX = (Camera.getX() / tileWidth) - 1;
		int startY = (Camera.getY() / tileHeight) - 1;
		for (int y = startY; y < (startY + height + 1); y++) {
			if (y >= height || y < 0) break;
			for (int x = startX; x < (startX + width + 1); x++) {
				if (x >= width || x < 0) break;
				tiles[x][y].render(this.x + x * tileWidth - Camera.getX(), this.y + y * tileHeight - Camera.getY(), g);
			}
		}
	}
}
