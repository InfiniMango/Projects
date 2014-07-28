package com.infinimango.flux.world;

import com.infinimango.flux.Display;
import com.infinimango.flux.Game;
import com.infinimango.flux.world.entity.Entity;

import java.util.Random;

public class Camera {
	// Entity to center the camera on
	static Entity followedEntity;
	// Location of the camera
	static float x = 0;
	static float y = 0;

	// Offset of centering the camera to an entity
	public static int followOffsetX = 0;
	public static int followOffsetY = 0;

	// Camera movement smoothing amount
	public static float smoothing = 1f;

	// Indicates if camera bounds are active to a certain direction
	static boolean borderUp = false;
	static boolean borderDown = false;
	static boolean borderLeft = false;
	static boolean borderRight = false;

	// Camera bounds limit
	static int up, down, left, right;

	// Shake-effect timer and variables
	static boolean shaking = false;
	static long shakeDelayer = 0;
	static final long SHAKE_DELAY = 20;
	static long shakeTimer = 0;
	static final long SHAKE_DECAY_TIME = 500;
	static int shakeAmount;
	static Random random = new Random();

	/**
	 * Updates the camera movement and shaking.
	 */
	public static void update(){
		if(followedEntity != null) {
			float ex = followedEntity.getX() + followOffsetX;
			float ey = followedEntity.getY() + followOffsetY;

			float changeX = (ex - Display.getWidth() / 2 + followedEntity.getWidth() / 2 - x) / smoothing;
			float changeY = (ey - Display.getHeight() / 2 + followedEntity.getHeight() / 2 - y) / smoothing;

			if(Math.abs(changeX) > (1.0f / smoothing)){
				x += changeX;
			}else{
				x = Math.round(ex - Display.getWidth() / 2 + followedEntity.getWidth() / 2);
			}

			if(Math.abs(changeY) > (1.0f / smoothing)){
				y += changeY;
			}else {
				y = Math.round(ey - Display.getHeight() / 2 + followedEntity.getHeight() / 2);
			}
		}

		if(borderUp && y < up) y = up;
		if (borderDown && y + Display.getHeight() > down) y = down - Display.getHeight();
		if(borderLeft && x < left) x = left;
		if (borderRight && x + Display.getWidth() > right) x = right - Display.getWidth();

		if(shaking){
			if(Game.getTime() - shakeDelayer > SHAKE_DELAY) {
				if (Game.getTime() - shakeTimer > SHAKE_DECAY_TIME) shaking = false;
				x += random.nextInt(shakeAmount * 2) - shakeAmount;
				y += random.nextInt(shakeAmount * 2) - shakeAmount;
				shakeDelayer = Game.getTime();
			}
		}
	}

	/**
	 * Moves camera up.
	 * @param amount Speed to move to
	 */
	public static void moveUp(float amount){
		y -= amount;
	}

	/**
	 * Moves camera down.
	 * @param amount Speed to move to
	 */
	public static void moveDown(float amount){
		y += amount;
	}

	/**
	 * Moves camera left.
	 * @param amount Speed to move to
	 */
	public static void moveLeft(float amount){
		x -= amount;
	}

	/**
	 * Moves camera right.
	 * @param amount Speed to move to
	 */
	public static void moveRight(float amount){
		x += amount;
	}

	/**
	 * Gets horizontal position of the camera.
	 * @return x-coordinate of the camera
	 */
	public static int getX(){
		return (int)x;
	}

	/**
	 * Gets vertical position of the camera.
	 * @return y-coordinate of the camera
	 */
	public static int getY(){
		return (int)y;
	}

	/**
	 * Repositions the camera.
	 * @param x new x-coordinate of the camera
	 * @param y new y-coordinate of the camera
	 */
	public void setLocation(float x, float y){
		Camera.x = x;
		Camera.y = y;
	}

	/**
	 * Creates an upper boundary for the camera.
	 * @param up boundary position
	 */
	public static void setLimitUp(int up){
		Camera.up = up;
		borderUp = true;
	}

	/**
	 * Creates a lower boundary for the camera.
	 * @param down boundary position
	 */
	public static void setLimitDown(int down){
		Camera.down = down;
		borderDown = true;
	}

	/**
	 * Creates a left boundary for the camera.
	 * @param left boundary position
	 */
	public static void setLimitLeft(int left){
		Camera.left = left;
		borderLeft = true;
	}

	/**
	 * Creates a right boundary for the camera.
	 * @param right boundary position
	 */
	public static void setLimitRight(int right){
		Camera.right = right;
		borderRight = true;
	}

	/**
	 * Removes cameras upper limit.
	 */
	public static void removeUpLimit(){
		borderUp = false;
	}

	/**
	 * Removes cameras lower limit.
	 */
	public static void removeDownLimit(){
		borderDown = false;
	}

	/**
	 * Removes cameras left limit.
	 */
	public static void removeLeftLimit(){
		borderLeft = false;
	}

	/**
	 * Removes cameras right limit.
	 */
	public static void removeRightLimit(){
		borderRight = false;
	}

	/**
	 * Removes all movement limitations of the camera.
	 */
	public static void removeLimits(){
		borderUp = false;
		borderDown = false;
		borderLeft = false;
		borderRight = false;
	}

	/**
	 * Pans the camera to a new location
	 * @param x new locations x-coordinate
	 * @param y new locations y-coordinate
	 */
	public static void moveTo(int x, int y){
		followedEntity = new Entity(x, y);
	}

	/**
	 * Makes the camera center on a certain entity.
	 * @param e Entity to follow
	 */
	public static void follow(Entity e){
		followedEntity = e;
	}

	/**
	 * Checks, if the camera is currently following an entity
	 * @return True, if camera is following an entity
	 */
	public static boolean isFollowing(){
		return followedEntity != null;
	}

	/**
	 * Creates a shake effect of a custom strength
	 * @param amount Strength of the shake in pixels
	 */
	public static void shake(int amount){
		Camera.shaking = true;
		shakeTimer = Game.getTime();
		shakeAmount = amount;
		shakeDelayer = Game.getTime();
	}

}
