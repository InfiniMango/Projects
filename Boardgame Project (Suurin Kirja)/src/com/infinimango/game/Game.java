package com.infinimango.game;

import com.infinimango.flux.Display;

public class Game extends com.infinimango.flux.Game {
	public static boolean debug = true;

	public static final int WIDTH = 640;
	public static final int HEIGHT = WIDTH / 4 * 3;
	public static final int SCALE = 1;

	/*
	 * COMMON ASPECT RATIOS: 4 * 3 - 8 * 5 - 16 * 9
	 */

	public static void main(String args[]) {
		Display display = new Display(new Game());
		display.setSize(WIDTH, HEIGHT);
		display.setScaling(SCALE);
		display.setTitle("Flux Game");

		display.setTargetUPS(60);
		display.setTargetFPS(60);

		display.showFPS(debug);

		display.create();

		Game.setState(new MapEditor());
	}

}
