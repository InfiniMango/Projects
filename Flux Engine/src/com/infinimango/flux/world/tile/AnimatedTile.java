package com.infinimango.flux.world.tile;

import com.infinimango.flux.graphics.Animation;

public class AnimatedTile extends Tile {
	// Animation of the tile
	Animation animation;

	/**
	 * Creates a new tile with a custom animation.
	 * 
	 * @param animation
	 *            Animation to display as a texture
	 * @param collides
	 *            Determines, whether the entities can pass trough the tile or
	 *            not
	 * @param data
	 *            Special tile data for custom use
	 */
	public AnimatedTile(Animation animation, boolean collides, int data) {
		super(animation == null ? null : animation.getCurrentFrame(), collides,
				data);
	}

	/**
	 * Update tiles animation and set animations current frame as a texture.
	 */
	public void update() {
		animation.update();
		setTexture(animation.getCurrentFrame());
	}

	public void setAnimation(Animation animation) {
		this.animation = animation;
	}

	public Animation getAnimation() {
		return animation;
	}
}
