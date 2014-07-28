package com.infinimango.flux;

import java.awt.*;

public abstract class State {
	public State() {
	}

	public abstract void update();

	public abstract void render(Graphics g);
}
