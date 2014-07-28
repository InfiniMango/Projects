package com.infinimango.flux.input;

import com.infinimango.flux.Debug;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class Keyboard extends KeyAdapter {
	// Holds the state of all keys
	private static boolean pressed[] = new boolean[256 * 256];

	public void keyPressed(KeyEvent e) {
		pressed[e.getKeyCode()] = true;
	}

	public void keyReleased(KeyEvent e) {
		pressed[e.getKeyCode()] = false;
	}

	/**
	 * Checks with the keycode if the according key is down.
	 * @param keyCode Id of the key to check(See: KeyEvent class for key codes)
	 * @return Returns true if the key is down
	 */
	public static boolean isKeyDown(int keyCode){
		return pressed[keyCode];
	}

	/**
	 * Resets the state of all keys in keyboard to not-pressed.
	 */
	public void reset(){
		for (int i = 0; i < pressed.length; i++) {
			pressed[i] = false;
		}
	}
}
