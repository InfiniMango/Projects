package com.infinimango.flux.world.entity;

import com.infinimango.flux.graphics.Animation;

public abstract class AnimatedItem extends Item {
	Animation animation;

	public AnimatedItem(float x, float y, Animation animation) {
		super(x, y, null);
		this.animation = animation;
		if (animation != null)
			setTexture(animation.getCurrentFrame());
	}

	public void updateAnimation() {
		animation.update();
		setTexture(animation.getCurrentFrame());
	}
}
