package com.infinimango.game;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;

import com.infinimango.flux.Debug;
import com.infinimango.flux.Resource;
import com.infinimango.flux.input.Mouse;
import com.infinimango.flux.world.Camera;

public class Hexagon {

	BufferedImage texture;
	static BufferedImage selector = Resource.loadImage("res/hex_sel.png");
	static BufferedImage highlighter = Resource.loadImage("res/hex_hl.png");

	public static final int TYPE_PINK = 1;
	public static final int TYPE_GREEN = 3;
	public static final int TYPE_BLUE = 2;

	public static final int TYPE_PINK_EXL = 4;
	public static final int TYPE_GREEN_EXL = 6;
	public static final int TYPE_BLUE_EXL = 5;

	float x, y;
	int type;

	boolean highlight;

	public Hexagon(float x, float y, int type) {
		this.x = x;
		this.y = y;
		this.type = type;

		updateTexture();
	}

	public boolean getHover() {
		float screenX = x - Camera.getX();
		float screenY = y - Camera.getY();

		if (Mouse.getX() > screenX && Mouse.getY() > screenY) {
			if (Mouse.getX() < screenX + texture.getWidth()
					&& Mouse.getY() < screenY + texture.getHeight()) {

				int realX = (int) (Mouse.getX() - screenX);
				int realY = (int) (Mouse.getY() - screenY);
				if (texture.getRGB(realX, realY) >> 24 != 0x00) {
					return true;
				}
			}
		}

		return false;
	}

	public void updateTexture() {
		switch (type) {
		case TYPE_PINK:
			texture = Resource.loadImage("res/hex_r.png");
			break;
		case TYPE_GREEN:
			texture = Resource.loadImage("res/hex_g.png");
			break;
		case TYPE_BLUE:
			texture = Resource.loadImage("res/hex_b.png");
			break;
		case TYPE_PINK_EXL:
			texture = Resource.loadImage("res/hex_r_exl.png");
			break;
		case TYPE_GREEN_EXL:
			texture = Resource.loadImage("res/hex_g_exl.png");
			break;
		case TYPE_BLUE_EXL:
			texture = Resource.loadImage("res/hex_b_exl.png");
			break;
		default:
			texture = null;
			Debug.error("Hexagon type is not initialized correctly: Use constants like Hexagon.TYPE_PINK");
		}
	}

	public void shiftColor(int shift) {
		boolean special = type > 3;

		type += shift;

		if (!special && type > 3)
			type = 0;
		if (type < 0)
			type = 0;
		if (special && type < 4)
			type = 6;
		if (type > 6)
			type = 4;

		updateTexture();
	}

	public int getWidth() {
		return texture.getWidth();
	}

	public int getHeight() {
		return texture.getHeight();
	}

	public Point getCenterPoint() {
		return new Point((Math.round(x + texture.getWidth() / 2)),
				(Math.round(y + texture.getHeight() / 2)));
	}

	public void setHighlight(boolean hightlight) {
		this.highlight = hightlight;
	}

	public void render(Graphics g) {
		if (texture == null)
			return;
		g.drawImage(texture, (Math.round(x - Camera.getX())),
				(Math.round(y - Camera.getY())), null);

		if (highlight) {
			g.drawImage(highlighter, Math.round(x - Camera.getX()),
					Math.round(y - Camera.getY()), null);
		}

		if (getHover()) {
			g.drawImage(
					selector,
					Math.round(x + getWidth() / 2 - selector.getWidth() / 2
							- Camera.getX()),
					(Math.round(y + getHeight() - selector.getHeight()
							- getHeight() / 5 - Camera.getY())), null);
		}
	}
}
