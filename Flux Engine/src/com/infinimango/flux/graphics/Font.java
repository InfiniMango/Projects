package com.infinimango.flux.graphics;

import java.awt.*;

public class Font {
	String symbols;
	SpriteSheet sprites;
	int symbolCount;

	Color color;
	boolean allCaps;

	public Font(String symbols, SpriteSheet sprites, boolean allCaps){
		this.symbols = symbols;
		this.sprites = sprites;
		this.allCaps = allCaps;

		symbolCount = symbols.length();
		color = Color.WHITE;
	}

	public void write(String text, int x, int y, Graphics g){
		if(allCaps) text = text.toUpperCase();

		for(int i = 0; i < text.length(); i++){
			if(text.charAt(i) != ' '){
				g.drawImage(get(text.charAt(i)), x + i * getSymbolWidth(), y, null);
			}
		}
	}

	public int getLength(String s){
		return s.length() * getSymbolWidth();
	}
	public int getHeight(){
		return getSymbolHeight();
	}

	public int getSymbolWidth(){
		return sprites.getSpriteWidth();
	}
	public int getSymbolHeight(){
		return sprites.getSpriteHeight();
	}

	private Image get(char symbol){
		for(int i = 0; i < symbols.length(); i++){
			if(symbols.charAt(i) == symbol) return sprites.extract(i + 1);
		}

		return sprites.extract(0);
	}

	public void setColor(Color color){
		if(color.getRGB() == this.color.getRGB()) return;
		sprites.replaceColor(this.color, color);
		this.color = color;
	}

}
