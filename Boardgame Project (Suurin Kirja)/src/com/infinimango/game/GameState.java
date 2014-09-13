package com.infinimango.game;

import java.awt.Color;
import java.awt.Graphics;

import com.infinimango.flux.State;

public class GameState extends State {
	String str;
	Reads r=new Reads();
	
	public GameState() {
		str=r.getContent();
		// TODO Initialize state
	}
	
	public void update() {
		// TODO Update state
	}
	
	public void render(Graphics g) {
		g.setColor(Color.white);
		g.drawString(str, 20, 30);
		// TODO Render state
	}
	
}
