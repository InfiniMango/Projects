package com.infinimango.game;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.Serializable;

import com.infinimango.flux.Resource;
import com.infinimango.flux.input.Mouse;
import com.infinimango.flux.world.Camera;
import com.infinimango.flux.world.entity.Entity;

public class Hex extends Entity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1182201815920324298L;
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
			.loadImage("res/hex_w2.png");

	private static float sAlpha = 0.5f;
	public static boolean glowUp = false;
	
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
		color = (((color % 3) + 3) % 3);
		updateTexture();
	}
	
	public void updateTexture(){
		setTexture(textures[color + (special ? 3 : 0)]);
	}
	
	public void setSpecial(boolean special){
		this.special = special;
		updateTexture();
	}

	public boolean isSpecial(){
		return special;
	}
	
	public void toggleSpecial(){
		setSpecial(!special);
	}
	
	public int getColor(){
		return color;
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
		
		sAlpha += glowUp? 0.0001f : -0.0001f;
		
		if(sAlpha > 0.6f && glowUp) {
			glowUp = false;
			sAlpha = 0.6f;
		}
		if(sAlpha < 0.2f && !glowUp) {
			glowUp = true;
			sAlpha = 0.2f;
		}
		
		if (selected) {
			alphaRender(selectionTexture, getScreenX(), getScreenY(), g);
		}
	}

	private void alphaRender(BufferedImage img, int x, int y, Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
				sAlpha));
		g2.drawImage(img, x, y, null);
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
				1));
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
	
	public void setX(int x){
		this.x = x;
	}
	
	public void setY(int y){
		this.y = y;
	}
}
