package com.infinimango.game;

import com.infinimango.flux.Display;

public class Game extends com.infinimango.flux.Game {
	public static boolean debug = true;

	public static final int WIDTH = 1024;
	public static final int HEIGHT = WIDTH / 8 * 5;
	public static final int SCALE = 1;

	/*
	 * COMMON ASPECT RATIOS: 4 * 3 - 8 * 5 - 16 * 9
	 */

	public static void main(String args[]) {
		Display display = new Display(new Game());
		display.setSize(WIDTH, HEIGHT);
		display.setScaling(SCALE);
		display.setTitle("Suurin Kirja");
		display.setAutoSleep(true);

		display.setTargetUPS(60);
		display.setTargetFPS(60);

		display.setFullscreen(true);

		display.showFPS(debug);

		display.create();

		Game.setState(new GameState());
	}

}
