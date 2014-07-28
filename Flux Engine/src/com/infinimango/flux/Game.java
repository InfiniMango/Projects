package com.infinimango.flux;

import java.awt.*;

public abstract class Game {
	private static State state;

	private static long time;

	public void updateClock(){
		Game.time = System.currentTimeMillis();
	}

	public static void setState(State state) {
		Game.state = state;
	}

	public static long getTime(){
		return time;
	}

	public void update() {
		if (state != null) state.update();
	}

	public void render(Graphics g) {
		if (state != null) state.render(g);
	}

	public static void quit(){
		Display.close();
	}
}
