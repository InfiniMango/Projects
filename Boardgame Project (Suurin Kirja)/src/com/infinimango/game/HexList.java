package com.infinimango.game;

import java.awt.Graphics;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class HexList extends ArrayList<Hex> {

	private static final long serialVersionUID = -594882807558738065L;

	public static HexList load(String path){
		File file = new File(path);
		if(!file.exists()) {
			System.err.println("FILE NOT FOUND - " + path);
		}
		HexList hexagons = null;
		
		try {
			ObjectInputStream o = null;
			try {
				FileInputStream s = new FileInputStream(file);
				o = new ObjectInputStream(s);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
	
			int amount = o.readInt();
			
			hexagons = new HexList();
			
			for(int i = 0; i < amount; i++){
				int x = o.readInt();
				int y = o.readInt();
				
				int x2 = o.readInt();
				int y2 = o.readInt();
				int z2 = o.readInt();
				
				int color = o.readInt();
				boolean special = o.readBoolean();
				hexagons.add(new Hex(x, y, color, special, new Point3D(x2, y2, z2)));
			}


			o.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return hexagons;
	}
	
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
	
	public void offsetAll(int x, int y){
		for (Hex hex : this) {
			hex.setX(hex.getX() + x);
			hex.setY(hex.getY() + y);
		}
	}
	
	public void addSpacingX(float x){
		for (Hex hex : this) {
			hex.setX((int)(hex.getX() + hex.getLocation().getX() * x));
		}
	}
}
