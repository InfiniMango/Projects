package com.infinimango.flux.input;

import com.infinimango.flux.Display;
import com.infinimango.flux.Game;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class WindowHandler implements WindowListener {
	public void windowOpened(WindowEvent e) {

	}

	public void windowClosing(WindowEvent e) {
		Display.close();
	}

	public void windowClosed(WindowEvent e) {
		System.exit(0);
	}

	public void windowIconified(WindowEvent e) {

	}

	public void windowDeiconified(WindowEvent e) {

	}

	public void windowActivated(WindowEvent e) {

	}

	public void windowDeactivated(WindowEvent e) {

	}
}
