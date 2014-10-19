package com.infinimango.game;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.infinimango.flux.Resource;
import com.infinimango.flux.State;

public class TestState extends State {
	BufferedImage img;

	float opacity = 0f;
	boolean rising = true;

	public TestState() {
		img = Resource.loadImage("res/the_boulder.png");
	}

	@Override
	public void update() {
		opacity += rising ? 0.01f : -0.01f;
		if (opacity > 1 && rising) {
			opacity = 1;
			rising = false;
		}

		if (opacity < 0 && !rising) {
			opacity = 0;
			rising = true;
		}
	}

	@Override
	public void render(Graphics g2) {
		Graphics2D g = (Graphics2D) g2;
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
				opacity));
		g.drawImage(img, 20, 20, null);

		g.setColor(Color.blue);
		g.fillRect(200, 200, 64, 64);
	}
}
