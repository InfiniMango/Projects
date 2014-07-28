package com.infinimango.flux.world.entity;

import com.infinimango.flux.Display;
import com.infinimango.flux.world.Camera;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Entity {
	// Coordinates, dimensions and texture of the entity
	protected float x, y;
	protected int width, height;
	private BufferedImage texture;

	/**
	 * Creates a new entity.
	 * @param x X Coordinate of the new entity
	 * @param y Y Coordinate of the new entity
	 * @param texture Texture image
	 */
	public Entity(float x, float y, BufferedImage texture){
		this.x = x;
		this.y = y;
		setTexture(texture);
	}

	/**
	 * Creates a new entity.
	 * @param x X Coordinate of the new entity
	 * @param y Y Coordinate of the new entity
	 */
	public Entity(float x, float y){
		this(x, y, null);
	}

	/**
	 * Render entity texture.
	 * @param g Canvas Graphics
	 */
	public void render(Graphics g){
		if(texture == null) return;
		g.drawImage(texture, getScreenX(), getScreenY(), null);
	}

	/**
	 * Set the entity's texture.
	 * @param texture new texture of the entity
	 */
	public void setTexture(BufferedImage texture){
		this.texture = texture;
		if(texture == null) return;
		width = texture.getWidth();
		height = texture.getHeight();
	}

	/**
	 * Get entity's horizontal location.
	 * @return Entity's x-coordinate
	 */
	public int getX(){
		return Math.round(x);
	}

	/**
	 * Get entity's vertical location.
	 * @return Entity's y-coordinate
	 */
	public int getY(){
		return Math.round(y);
	}

	/**
	 * Get entity's width in pixels.
	 * @return Entity width
	 */
	public int getWidth(){
		return width;
	}

	/**
	 * Get entity's height in pixels.
	 * @return Entity height
	 */
	public int getHeight(){
		return height;
	}

	/**
	 * Get entity's horizontal location on screen.
	 * @return Entity's x-coordinate according to camera
	 */
	public int getScreenX(){
		return Math.round(x) - Camera.getX();
	}

	/**
	 * Get entity's vertical location on screen.
	 * @return Entity's y-coordinate according to camera
	 */
	public int getScreenY(){
		return Math.round(y) - Camera.getY();
	}

	/**
	 * Checks, if the entity overlaps with the current screen area.
	 * @return returns true if the entity is visible on the screen
	 */
	public boolean isOnScreen() {
		boolean left = x + width > Camera.getX();
		boolean right = x < Camera.getX() + Display.getWidth();
		boolean up = y + height > Camera.getY();
		boolean down = y < Camera.getY() + Display.getHeight();

		return (left && right && up && down);
	}

	/**
	 * Checks if another entity is touching this entity.
	 * @param e Entity to check
	 * @return True, if entities are overlapping
	 */
	public boolean intersects(Entity e){
		return !(e.getX() > getX() + getWidth() || e.getX() + e.getWidth() < getX() ||
				e.getY() > getY() + getHeight() || e.getY() + e.getHeight() < getY());
	}

	/**
	 * Checks if a point is within this entity's bounds.
	 * @param x X Coordinate of the point
	 * @param y Y Coordinate of the point
	 * @return True, if point is inside entity
	 */
	public boolean includes(float x, float y){
		return x > getX() && y > getY() && x < getX() + getWidth() && y < getY() + getHeight();
	}
}
