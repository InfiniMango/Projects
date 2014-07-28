package com.infinimango.flux.world.entity;

import com.infinimango.flux.Game;
import com.infinimango.flux.Resource;
import com.infinimango.flux.world.World;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Creature extends Entity {
	public float FULL_HEALTH = 1;
	private float health = FULL_HEALTH;

	private float factor = 1;

	BufferedImage flashTexture;

	private Color flashColor;

	private long flashTimer;
	private long flashDuration = 75;

	public Creature(float x, float y, BufferedImage texture) {
		super(x, y, texture);
		flashColor = Color.WHITE;
	}

	protected Creature(float x, float y){
		this(x, y, null);
	}

	public void heal(float amount){
		health += amount / factor;
		if(health > FULL_HEALTH) health = FULL_HEALTH;
	}

	public void hurt(float amount){
		health -= amount / factor;
		if(health < 0) health = 0;
		flashTimer = Game.getTime();
	}

	public abstract void update(World world);

	public void setTexture(BufferedImage texture){
		super.setTexture(texture);
		if(texture == null) return;
		flashTexture = Resource.getImageSilhouette(texture, flashColor);
	}

	public void render(Graphics g){
		super.render(g);
		if(flashTexture == null) return;
		if(Game.getTime() - flashTimer < flashDuration){
			g.drawImage(flashTexture, getScreenX(), getScreenY(), null);
		}
	}

	public void setHealthMultiplier(float factor){
		this.factor = factor;
	}

	public void setFlashDuration(long flashDuration){
		this.flashDuration = flashDuration;
	}

	public void kill(){
		health = 0;
	}

	public boolean isAlive(){
		return health > 0;
	}

	public boolean isDead(){
		return health == 0;
	}

	public void setFlashColor(Color color){
		flashColor = color;
	}
}
