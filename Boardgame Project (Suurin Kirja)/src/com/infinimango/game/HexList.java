package com.infinimango.game;

import java.awt.Graphics;
import java.util.ArrayList;

public class HexList extends ArrayList<Hex> {

	private static final long serialVersionUID = -594882807558738065L;

	public void shiftColors(int shift) {
		for (Hex hexagon : this) {
			hexagon.shiftColor(shift);
		}
	}

	public void render(Graphics g) {
		for (Hex hex : this) {
			hex.render(g);
		}
	}

	public Hex getHexAt(Point3D point) {
		for (Hex hex : this) {
			if (hex.getLocation().matches(point)) {
				return hex;
			}
		}
		return null;
	}

	public Hex getHexAt(int x, int y, int z) {
		return getHexAt(new Point3D(x, y, z));
	}
}
