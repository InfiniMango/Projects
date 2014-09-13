package com.infinimango.game;

import com.infinimango.flux.Display;

public class Game extends com.infinimango.flux.Game {

	public Game() {
		Display display = new Display(this);

		display.setSize(420, 360);
		display.setScaling(2);

		display.setAutoSleep(true);
		display.showFPS(true);

		display.setTargetUPS(60);
		display.setTargetFPS(60);

		display.create();
	}

	public static void main(String args[]) {
		new Game();
	}

}
