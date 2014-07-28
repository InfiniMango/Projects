package com.infinimango.flux.world.entity;

import java.awt.image.BufferedImage;

public abstract class Item extends Entity {
	// Item pickup state
	boolean pickedUp = false;

	/**
	 * Creates a new item.
	 * @param x       X Coordinate of the new entity
	 * @param y       Y Coordinate of the new entity
	 * @param texture Texture image
	 */
	public Item(float x, float y, BufferedImage texture) {
		super(x, y, texture);
	}

	public boolean isPickedUp(){
		return pickedUp;
	}

	public void pickedUpBy(Player player){
		pickedUp = true;
		onPickUp(player);
	}

	public abstract void onPickUp(Player player);
}
