package com.infinimango.game;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.Random;

import com.infinimango.flux.Display;
import com.infinimango.flux.Resource;
import com.infinimango.flux.State;
import com.infinimango.flux.graphics.SpriteSheet;
import com.infinimango.flux.input.Keyboard;
import com.infinimango.flux.input.Mouse;
import com.infinimango.flux.world.Camera;

public class GameState extends State {
	String str;
//	Reads r = new Reads();

//	HexaMap map;
	HexList map;

	int cameraSpeed = 4;

	boolean guiToggle = true;
	boolean togglePressed;

	BufferedImage background;
	
	BufferedImage dice;
	BufferedImage diceGlow;
	
	boolean glowDir;
	float glowA;
	
	boolean throwing;
	
	long throwTimer;
	final long throwDelay = 5000;
	long throwRandTimer;
	final long throwRandDel = 500;
	int currRand = 0;
	
	Player player;
	Player selectedPlayer = null;

	SpriteSheet numbers;
	SpriteSheet numbers2;
	
	//
	// int tx, ty;
	
	boolean mouseLeft;

	public GameState() {
//		str = r.getContent();

		background = Resource.loadImage("res/board.png");
		
		dice = Resource.loadImage("res/dice.png");
		diceGlow = Resource.loadImage("res/dice_bg.png");
//		map = new HexaMap(92, 50, "map_default", 22, 13, 48.5f, 56.4f);
		map = HexList.load("default_map");
		map.offsetAll(190, 108);
		map.addSpacingX(-0.5f);
		
		Camera.setLimitLeft(0);
		Camera.setLimitUp(0);
		Camera.setLimitRight(background.getWidth() + Display.getWidth() / 4);
		Camera.setLimitDown(background.getHeight() + Display.getHeight() / 4);
		
		player = new Player(map.getHexAt(0, 0, 0), Player.BLUE);
		
		numbers = new SpriteSheet(Resource.loadImage("res/numbers.png"), 64, 64);
		numbers2 = new SpriteSheet(Resource.loadImage("res/numbers2.png"), 64, 64);
	}

	@Override
	public void update() {
		// Camera movement
		if (Keyboard.isKeyDown(KeyEvent.VK_W))
			Camera.moveUp(cameraSpeed);
		if (Keyboard.isKeyDown(KeyEvent.VK_S))
			Camera.moveDown(cameraSpeed);
		if (Keyboard.isKeyDown(KeyEvent.VK_A))
			Camera.moveLeft(cameraSpeed);
		if (Keyboard.isKeyDown(KeyEvent.VK_D))
			Camera.moveRight(cameraSpeed);

		if (!togglePressed && Keyboard.isKeyDown(KeyEvent.VK_G)) {
			guiToggle = !guiToggle;
			togglePressed = true;
		}

		if (!Keyboard.isKeyDown(KeyEvent.VK_G))
			togglePressed = false;

		Camera.update();

		if (Keyboard.isKeyDown(KeyEvent.VK_ESCAPE))
			Display.close();

		// if (Keyboard.isKeyDown(KeyEvent.VK_LEFT))
		// tx--;
		// if (Keyboard.isKeyDown(KeyEvent.VK_UP))
		// ty--;
		// if (Keyboard.isKeyDown(KeyEvent.VK_RIGHT))
		// tx++;
		// if (Keyboard.isKeyDown(KeyEvent.VK_DOWN))
		// ty++;
		//
		// if (Keyboard.isKeyDown(KeyEvent.VK_T))
		// System.out.println("x: " + tx + " - y: " + ty);

		// Rectangle r = new Rectangle();
		// boolean isMouseOnButton = r.contains(new Point(Mouse.getX(), Mouse
		// .getY()));

//		map.update();
		
		if ((!mouseLeft && Mouse.buttonDown(Mouse.LEFT))) {
			for (Hex hex : map) {
				if (hex.getHover()) {
					if(player.getLocation().matches(hex.getLocation())){
						selectedPlayer = player;
						for(Hex hex2 : map){
							hex2.setSelected(false);
						}
						hex.setSelected(true);
						for(Hex hex2 : map){
							if(hex2.getHexDistanceTo(hex) <= 1) hex2.setSelected(true); 
						}
						break;
					}else{
						if(selectedPlayer != null && selectedPlayer == player){
							if(hex.getLocation().getHexDistanceTo(player.getLocation()) == 1){
								player.moveTo(hex);
							}
						}
					}
				}
			}
		}
		
		if (!Mouse.buttonDown(Mouse.LEFT))
			mouseLeft = false;
		
		if(throwing){
			if(Game.getTime() - throwRandTimer > throwRandDel){
				throwRandTimer = Game.getTime();
				int random = new Random().nextInt(3);
				while(random == currRand){
					random = new Random().nextInt(3);
				}
				currRand = random;
			}
		}
		
		player.update();
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(background, -Camera.getX(), -Camera.getY(), null);

//		map.render(92, 50, g);

		map.render(g);
		
		player.render(g);
		
		if (guiToggle) {
			g.setColor(new Color(108, 72, 32));
			int w = Display.getWidth();
			int h = Display.getHeight();
			g.fillRect(w - w / 4, 0, w / 2, h);
			g.fillRect(0, h - h / 4, w, h / 4);
			
			// DICE CONTAINER
			int bw = 32;
			
			int mbx = bw;
			int mby = h - h / 4 + bw;
			
			int mbx2 = mbx + h / 4 - bw * 2;
			int mby2 = h - bw;
			
			if(Mouse.getX() > mbx && Mouse.getY() > mby && Mouse.getX() < mbx2 && Mouse.getY() < mby2){
				g.setColor(new Color(168, 102, 72, 128));
				g.fillRect(mbx, mby, mbx2 - mbx, mby2 - mby);
				
				if(Mouse.buttonDown(Mouse.LEFT) && !throwing){
					throwing = true;
					throwTimer = Game.getTime();
					throwRandTimer = Game.getTime();
				}
			}
			
			// DICE GLOW BG & DICE
			glowA += glowDir? 0.05f : -0.05f;
			
			if(glowA > 1 && glowDir) {
				glowA = 1;
				glowDir = false;
			}
			
			if(glowA < 0 && !glowDir) {
				glowA = 0;
				glowDir = true;
			}
			
			alphaRender(diceGlow, h / 8 - diceGlow.getWidth() / 2, h - h / 8 - diceGlow.getHeight() / 2, glowA, g);
			g.drawImage(dice, h / 8 - dice.getWidth() / 2, h - h / 8 - dice.getHeight() / 2, null);
			
			if(throwing){
				g.drawImage(numbers.extract(currRand), h / 8 + dice.getWidth() / 2, h - h / 8 - dice.getHeight() / 2, null);
			}
		}

//		g.setColor(Color.black);
//		g.fillRect(0, 0, 300, 100);

//		if (HexaMap.one) {
//			g.setColor(Color.blue);
//			g.drawString("POINT 1 - BLUE IN x: " + HexaMap.oneX + ", y: "
//					+ HexaMap.oneY, 0, 20);
//		}
//
//		if (HexaMap.two) {
//			g.setColor(Color.red);
//			g.drawString("POINT 2 - RED IN x: " + HexaMap.twoX + ", y: "
//					+ HexaMap.twoY, 0, 40);
//		}
//
//		if (HexaMap.one && HexaMap.two) {
//			g.setColor(Color.white);
//			double dist = Math.sqrt(Math.abs(HexaMap.oneX - HexaMap.twoX)
//					+ Math.abs(HexaMap.oneY - HexaMap.twoY));
//			g.drawString("Distance in 2D: " + Math.ceil(dist + 1) + " (" + dist
//					+ ")", 0, 60);
//		}

		// g.setColor(Color.white);
		// g.drawString(str, 20, 30);
	}
	
	private void alphaRender(BufferedImage img, int x, int y, float a, Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
				a));
		g2.drawImage(img, x, y, null);
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
				1));
	}
}
