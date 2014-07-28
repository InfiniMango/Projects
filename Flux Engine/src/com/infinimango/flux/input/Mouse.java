package com.infinimango.flux.input;

import java.awt.*;
import java.awt.event.*;

public class Mouse extends MouseAdapter {
	// Mouse button constants
	public static final int LEFT = 1;
	public static final int MIDDLE = 2;
	public static final int RIGHT = 3;
	// State of all mouse buttons
	private static boolean buttons[] = new boolean[MouseInfo.getNumberOfButtons()];
	// Indicates if the mouse is currently on the screen
	private static boolean onScreen = false;
	// Coordinates of the mouse
	private static int x, y;

	// Rotation of the wheel
	private static int wheelRotation;

	// Scale amount of the coordinates according to the pixel scale
	private static int scale;

	/**
	 * Initializes the mouse adapter.
	 * @param scale Pixel scaling of the game for realistic coordinates
	 */
	public Mouse(int scale){
		Mouse.scale = scale;
	}

	public void mousePressed(MouseEvent e) {
		buttons[e.getButton()] = true;
		updateLocation(e);
	}

	public void mouseReleased(MouseEvent e) {
		buttons[e.getButton()] = false;
		updateLocation(e);
	}

	public void mouseEntered(MouseEvent e) {
		onScreen = true;
		updateLocation(e);
	}

	public void mouseExited(MouseEvent e) {
		onScreen = false;
		updateLocation(e);
	}

	public void mouseDragged(MouseEvent e) {
		updateLocation(e);
	}

	public void mouseMoved(MouseEvent e) {
		updateLocation(e);
	}

	public void mouseWheelMoved(MouseWheelEvent e) {
		wheelRotation += e.getWheelRotation();
	}

	/**
	 * Updates the mouse coordinates.
	 * @param e MouseEvent which includes the new coordinates
	 */
	private void updateLocation(MouseEvent e){
		x = e.getX() / scale;
		y = e.getY() / scale;
	}

	/**
	 * Get mouses horizontal location on the screen
	 * @return Mouse x-coordinate
	 */
	public static int getX(){
		return x;
	}

	/**
	 * Get mouses vertical location on the screen
	 * @return Mouse y-coordinate
	 */
	public static int getY(){
		return y;
	}

	/**
	 * Checks if a mouse button is currently down.
	 * @param id Which button to check for
	 * @return True, if the button is down
	 */
	public static boolean buttonDown(int id){
		return buttons[id];
	}

	/**
	 * Checks if the cursor is on the screen.
	 * @return True, if cursor is within the screen bounds
	 */
	public static boolean isOnScreen(){
		return onScreen;
	}

	/**
	 * Returns the current wheel rotation. Note! Wheel rotation does not
	 * reset itself, so you must use a variable to determine the change for scrolling,
	 * or use the reset()-method to reset mouse wheel(and buttons).
	 * @return Current rotation of the wheel
	 */
	public static int getWheelRotation(){
		return wheelRotation;
	}

	/**
	 * Resets button states back to false and sets wheel rotation back to 0.
	 */
	public static void reset(){
		for (int i = 0; i < buttons.length; i++) {
			buttons[i] = false;
		}
		wheelRotation = 0;
	}
}
