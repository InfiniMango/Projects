package com.infinimango.flux.world.tile;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.infinimango.flux.world.Camera;

public class Tile {
	// Texture of the tile
	private BufferedImage texture;
	// Tells if one can pass trough the tile
	private boolean collides;
	// Special data stored in the tile for game specific use
	private int data;

	/**
	 * Creates a new tile with custom appearance, collision and data.
	 * 
	 * @param texture
	 *            Texture image of the tile
	 * @param collides
	 *            Tells if one can pass trough the tile
	 * @param data
	 *            Special data stored in the tile dor game specific use
	 */
	public Tile(BufferedImage texture, boolean collides, int data) {
		this.texture = texture;
		this.collides = collides;
		this.data = data;
	}

	/**
	 * Render tile to the screen.
	 * 
	 * @param x
	 *            World x of the tile
	 * @param y
	 *            World y of the tile
	 * @param g
	 *            Canvas Graphics
	 */
	public void render(int x, int y, Graphics g) {
		if (texture == null)
			return;
		g.drawImage(texture, x - Camera.getX(), y - Camera.getY(), null);
	}

	/**
	 * Update tiles texture image
	 * 
	 * @param texture
	 *            new texture for replacing the old one
	 */
	public void setTexture(BufferedImage texture) {
		this.texture = texture;
	}

	/**
	 * Checks if the tile has collision data in it
	 * 
	 * @return Returns true if the tile collides with entities
	 */
	public boolean collides() {
		return collides;
	}

	/**
	 * Get special data stored in the tile
	 * 
	 * @return Tiles data value
	 */
	public int getData() {
		return data;
	}
}
