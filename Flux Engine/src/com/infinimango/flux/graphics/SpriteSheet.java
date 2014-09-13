package com.infinimango.flux.graphics;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Arrays;

import com.infinimango.flux.Resource;

public class SpriteSheet {
	// Size of sprites and amount of sprites in the sheet
	private int spriteWidth, spriteHeight, size;

	private BufferedImage spriteImage;

	// Sprite textures
	private BufferedImage sprites[];

	/**
	 * Creates a new sheet of sprite images.
	 * 
	 * @param image
	 *            Image to extract the sprites from
	 * @param spriteWidth
	 *            Width of one sprite
	 * @param spriteHeight
	 *            Height of one sprite
	 */
	public SpriteSheet(BufferedImage image, int spriteWidth, int spriteHeight) {
		this.spriteWidth = spriteWidth;
		this.spriteHeight = spriteHeight;
		this.spriteImage = image;

		size = (image.getWidth() / spriteWidth)
				* (image.getHeight() / spriteHeight);
		sprites = new BufferedImage[size];

		for (int i = 0; i < size; i++) {
			int spriteX = (i * spriteWidth) % image.getWidth();
			int spriteY = (i * spriteWidth) / image.getWidth() * spriteHeight;
			sprites[i] = image.getSubimage(spriteX, spriteY, spriteWidth,
					spriteHeight);
		}
	}

	/**
	 * Creates a new sheet of sprite images from an existing image array.
	 * 
	 * @param image
	 *            Image array to get the sprite images from
	 * @param spriteWidth
	 *            Width of one sprite
	 * @param spriteHeight
	 *            Height of one sprite
	 */
	public SpriteSheet(BufferedImage image[], int spriteWidth, int spriteHeight) {
		this.spriteWidth = spriteWidth;
		this.spriteHeight = spriteHeight;

		size = image.length;
		sprites = new BufferedImage[size];

		for (int i = 0; i < size; i++) {
			sprites[i] = image[i];
		}
	}

	/**
	 * Extracts a single image from the sheet.
	 * 
	 * @param index
	 *            Index of the image to extract
	 * @return Extracted image
	 */
	public BufferedImage extract(int index) {
		return sprites[index];
	}

	public BufferedImage getSpriteAt(int x, int y) {
		return sprites[y * (spriteImage.getWidth() / spriteWidth) + x];
	}

	public BufferedImage getSpriteAt(Point point) {
		return getSpriteAt((int) point.getX(), (int) point.getY());
	}

	/**
	 * Extracts multiple images from the sheet and puts the into an array.
	 * 
	 * @param startIndex
	 *            Where to start copying of images
	 * @param endIndex
	 *            Where to end copying of images
	 * @return
	 */
	public BufferedImage[] extract(int startIndex, int endIndex) {
		return Arrays.copyOfRange(sprites, startIndex, endIndex);
	}

	/**
	 * Replaces single color in all the sprites with another color.
	 * 
	 * @param c1
	 *            Color to swap from
	 * @param c2
	 *            Color to swap to
	 */
	public void replaceColor(Color c1, Color c2) {
		for (int i = 0; i < size; i++) {
			Resource.replaceImageColor(sprites[i], c1, c2);
		}
	}

	/**
	 * Gets the size of the sprite sheet in sprites.
	 * 
	 * @return Size of the sprite sheet
	 */
	public int getSize() {
		return size;
	}

	/**
	 * Gets the width of a single sprite in pixels.
	 * 
	 * @return Sprite width
	 */
	public int getSpriteWidth() {
		return spriteWidth;
	}

	/**
	 * Gets the height of a single sprite in pixels.
	 * 
	 * @return Sprite height
	 */
	public int getSpriteHeight() {
		return spriteHeight;
	}

	public void addSprite(BufferedImage image) {
		size++;
		BufferedImage[] sprites2 = new BufferedImage[size];

		for (int i = 0; i < size - 1; i++) {
			sprites2[i] = sprites[i];
		}

		sprites2[size] = image;

		sprites = new BufferedImage[size];
		sprites = sprites2.clone();
	}

}
