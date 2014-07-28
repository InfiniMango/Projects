package com.infinimango.flux.state;

import java.awt.*;

public abstract class State {
	/**
	 * Creates a new game state and runs the initializer.
	 */
	public State(){
		init();
	}

	/**
	 * Loads the needed resources and data.
	 */
	public abstract void init();

	/**
	 * Updates state logic.
	 */
	public abstract void update();

	/**
	 * Renders state to the canvas.
	 * @param g Canvas Graphics
	 */
	public abstract void render(Graphics g);

}
