package com.infinimango.flux.world.entity;

import com.infinimango.flux.graphics.Animation;

public class AnimatedEntity extends Entity {
	Animation animation;

	public AnimatedEntity(float x, float y, Animation animation) {
		super(x, y);
		this.animation = animation;
		if (animation != null)
			setTexture(animation.getCurrentFrame());
	}

	public void updateAnimation() {
		animation.update();
		setTexture(animation.getCurrentFrame());
	}
}
