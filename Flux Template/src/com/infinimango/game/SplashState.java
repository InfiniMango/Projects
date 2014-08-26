package com.infinimango.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.infinimango.flux.Display;
import com.infinimango.flux.Resource;
import com.infinimango.flux.State;

public class SplashState extends State {
	public static final int	NORMAL_LOGO_WIDTH	= 640;
	
	BufferedImage			background;
	
	public SplashState() {
		String postFix = "";
		int w = Display.getWidth();
		
		if (w >= 1280) postFix = "_xhd";
		if (w < 1280) postFix = "_hd";
		if (w < 640) postFix = "_ld";
		if (w < 480) postFix = "_xld";
		
		background = Resource.loadImage("res/mangologo_v3" + postFix + ".png");
	}
	
	public void update() {}
	
	public void render(Graphics g) {
		g.setColor(new Color(48, 48, 48));
		g.fillRect(0, 0, Display.getWidth(), Display.getHeight());
		
		g.drawImage(background, Display.getWidth() / 2 - background.getWidth()
				/ 2, Display.getHeight() / 2 - background.getHeight() / 2, null);
	}
	
}
