package com.infinimango.flux.graphics;

import com.infinimango.flux.Game;

import java.awt.image.BufferedImage;

import java.awt.*;

public class Animation {
	// Frames of the animation
	SpriteSheet frames;

	// Current frame index
	int currentFrame;

	// Timer for animating
	long timer;
	long delay = 100;

	// Playing state
	boolean playing = true;
	boolean playingOnce = false;

	/**
	 * Creates a new animation from a sheet of sprites.
	 * @param frames Sprites to create the animation from
	 */
	public Animation(SpriteSheet frames){
		this.frames = frames;
		timer = Game.getTime();
	}

	/**
	 * Creates a new animation from a sheet of sprites with a custom delay.
	 * @param frames Sprites to create the animation from
	 * @param delay Interval between frames
	 */
	public Animation(SpriteSheet frames, long delay){
		this(frames);
		this.delay = delay;
	}

	/**
	 * Creates a new animation from an array of images.
	 * @param images Array to create animation from
	 * @param width With of a single frame
	 * @param height Height of a single frame
	 */
	public Animation(BufferedImage images[], int width, int height){
		this(new SpriteSheet(images, width, height));
	}

	/**
	 * Creates a new animation from an array of images with a custom delay.
	 * @param images Array to create animation from
	 * @param width With of a single frame
	 * @param height Height of a single frame
	 * @param delay Interval between frames
	 */
	public Animation(BufferedImage images[], int width, int height, long delay){
		this(images, width, height);
		this.delay = delay;
	}

	/**
	 * Toggle animation looping. By default, the animation loops.
	 */
	public void playOnce(){
		playingOnce = true;
	}

	/**
	 * Resets the animation, so that it may be played again.
	 */
	public void reset(){
		currentFrame = 0;
		timer = Game.getTime();
		playing = true;
	}

	/**
	 * Updates the animation frame according to animation timer and updates the state of the animation.
	 */
	public void update(){
		if(playing && Game.getTime() - timer > delay){
			currentFrame++;
			if(currentFrame >= frames.getSize()){
				if(playingOnce) {
					playing = false;
					return;
				}
				currentFrame = 0;
			}

			timer = Game.getTime();
		}
	}

	/**
	 * Gets the current animation frame as an image.
	 * @return Current frame image
	 */
	public BufferedImage getCurrentFrame(){
		return frames.extract(currentFrame % frames.getSize());
	}

	/**
	 * Render the animation to the screen.
	 * @param x Location of the animation in the x-axis
	 * @param y Location of the animation in the y-axis
	 * @param g Canvas Graphics
	 */
	public void render(int x, int y, Graphics g){
		if(currentFrame >= frames.getSize()) return;
		if(playingOnce && !playing) return;
		g.drawImage(frames.extract(currentFrame), x, y, null);
	}

}
