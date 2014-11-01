package com.infinimango.game;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.infinimango.flux.Resource;
import com.infinimango.flux.input.Mouse;
import com.infinimango.flux.world.Camera;
import com.infinimango.flux.world.entity.Entity;

public class Hex extends Entity {
	int color;
	public static final int RED = 0;
	public static final int GREEN = 1;
	public static final int BLUE = 2;

	private static final BufferedImage[] textures = {
			Resource.loadImage("res/hex_r.png"),
			Resource.loadImage("res/hex_g.png"),
			Resource.loadImage("res/hex_b.png"),
			Resource.loadImage("res/hex_r_exl.png"),
			Resource.loadImage("res/hex_g_exl.png"),
			Resource.loadImage("res/hex_b_exl.png") };

	private static final BufferedImage selectionTexture = Resource
			.loadImage("res/hex_hl.png");

	boolean special;

	Point3D location;

	boolean selected;

	public Hex(float x, float y, int color, boolean special, Point3D location) {
		super(x, y);
		this.color = color;
		this.special = special;
		this.location = location;
		setTexture(textures[color + (special ? 3 : 0)]);
	}

	public Hex(float x, float y, BufferedImage texture, Point3D location) {
		super(x, y);
		color = 0;
		this.location = location;
		setTexture(texture);
	}

	public void shiftColor(int shift) {
		color += shift;
		color = color > 2 ? 0 : color;
		color = color < 0 ? 2 : color;
		setTexture(textures[color + (special ? 3 : 0)]);
	}

	public boolean getHover() {
		int hoverX = Mouse.getX() + Camera.getX();
		int hoverY = Mouse.getY() + Camera.getY();
		if (this.includes(hoverX, hoverY)) {
			if (getTexture().getRGB(Mouse.getX() - getScreenX(),
					Mouse.getY() - getScreenY()) >> 24 != 0x00) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void render(Graphics g) {
		super.render(g);
		if (selected) {
			g.drawImage(selectionTexture, getScreenX(), getScreenY(), null);
		}
	}

	public Point3D getLocation() {
		return location;
	}

	public int getHexDistanceTo(Hex hex) {
		return location.getHexDistanceTo(hex.location);
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
}
