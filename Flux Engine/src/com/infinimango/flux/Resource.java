package com.infinimango.flux;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.Buffer;

public class Resource {
	/**
	 * Load an image resource for use in game.
	 * @param path Image path in jar
	 * @return Loaded image
	 */
	public static BufferedImage loadImage(String path){
		BufferedImage image = null;
		try {
		 	image = ImageIO.read(new FileInputStream(path));
		} catch (IOException e) {
			Debug.error("Image at \"" + path + "\" could not be loaded\n");
			e.printStackTrace();
		}
		if(image == null) Debug.error("Image at \"" + path + "\" not found\n");
		return image;
	}

	/**
	 * Load a sound resource for use in game.
	 * @param path Sound path in jar
	 * @return Loaded sound
	 */
	public static Sound loadSound(String path){
		return Sound.load(path);
	}

	/**
	 * Creates an identical copy of an image.
	 * @param image Image to clone
	 * @return Copy of the image
	 */
	public static BufferedImage getImageCopy(BufferedImage image){
		ColorModel cm = image.getColorModel();
		boolean isAlphaPreMultiplied = cm.isAlphaPremultiplied();
		WritableRaster raster = image.copyData(null);
		return new BufferedImage(cm, raster, isAlphaPreMultiplied, null);
	}

	/**
	 * Creates a one-colored silhouette of the image based on alpha channel.
	 * @param image Image to make the silhouette of
	 * @param rgb Color of the silhouette in an integer value
	 * @return Image silhouette
	 */
	public static BufferedImage getImageSilhouette(BufferedImage image, int rgb){
		BufferedImage image2 = getImageCopy(image);
		for (int y = 0; y < image2.getHeight(); y++) {
			for (int x = 0; x < image2.getWidth(); x++) {
				if((image2.getRGB(x, y) & 0x000000ff) > 0){
					image2.setRGB(x, y, rgb);
				}
			}
		}
		return image2;
	}

	/**
	 * Creates a one-colored silhouette of the image based on alpha channel.
	 * @param image Image to make the silhouette of
	 * @param color Color of the silhouette
	 * @return Image silhouette
	 */
	public static BufferedImage getImageSilhouette(BufferedImage image, Color color){
		return getImageSilhouette(image, color.getRGB());
	}

	/**
	 * Replace one color in an image with another.
	 * @param image Image to swap colors in
	 * @param rgb1 Color to swap from in integer value
	 * @param rgb2 Color to swap to in integer value
	 */
	public static void replaceImageColor(BufferedImage image, int rgb1, int rgb2){
		for(int y = 0; y < image.getHeight(); y++){
			for(int x = 0; x < image.getWidth(); x++){
				if(image.getRGB(x, y) == rgb1) image.setRGB(x, y, rgb2);
			}
		}
	}

	/**
	 * Replace one color in an image with another.
	 * @param image Image to swap colors in
	 * @param color1 Color to swap from
	 * @param color2 Color to swap to
	 */
	public static void replaceImageColor(BufferedImage image, Color color1, Color color2){
		replaceImageColor(image, color1.getRGB(), color2.getRGB());
	}
}
