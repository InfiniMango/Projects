package com.infinimango.game;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.infinimango.flux.Resource;
import com.infinimango.flux.world.Camera;

public class Player{
	float rx, ry;
	Point3D location;
	Hex hex;
	
	// rx, ry = rendering x, -y
	// cx, cy = centering compensation x, -y
	
	public BufferedImage texture;
	
	public static final int RED = 0;
	public static final int GREEN = 1;
	public static final int BLUE = 2;
	
	int cx = 0;
	int cy = 0;
	
	public static final BufferedImage[] textures = {
		Resource.loadImage("res/player_r.png"),
		Resource.loadImage("res/player_g.png"),
		Resource.loadImage("res/player_b.png")
	};
	
	public Player(Hex hex, int color){
		texture = textures[color];
		if(hex != null){
			rx = hex.getX();
			ry = hex.getY();
			location = hex.getLocation();
			
			cx = hex.getWidth() / 2 - texture.getWidth() / 2;
			cy = hex.getHeight() / 2 - texture.getHeight() / 2;
		}
	}
	
	public void update(){
		if(hex == null) return;
		int dx = (int)(Math.abs(hex.getX() - rx));
		int dy = (int)(Math.abs(hex.getY() - ry));
		if(dx > 0 || dy > 0){
			rx += (hex.getX() - rx) / 6;
			ry += (hex.getY() - ry) / 6;
		}else{
			rx = hex.getX();
			ry = hex.getY();
		}
	}
	
	public void moveTo(Hex hex){
		this.hex = hex;
		location = hex.getLocation();
		cx = hex.getWidth() / 2 - texture.getWidth() / 2;
		cy = hex.getHeight() / 2 - texture.getHeight() / 2;
	}
	
	public void render(Graphics g){
		g.drawImage(texture, (int)(rx - Camera.getX() + cx), (int)(ry - Camera.getY() + cy), null);
	}
	
	public Point3D getLocation(){
		return location;
	}
	
}
