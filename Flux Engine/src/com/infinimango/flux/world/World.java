package com.infinimango.flux.world;

import com.infinimango.flux.world.entity.Creature;
import com.infinimango.flux.world.entity.Entity;
import com.infinimango.flux.world.entity.Item;
import com.infinimango.flux.world.entity.Player;
import com.infinimango.flux.world.tile.AnimatedTile;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class World {
	// Lists of all entities handled by the World
	public List<Entity> entities = new ArrayList<Entity>();
	public List<Creature> creatures = new ArrayList<Creature>();
	public List<Item> items = new ArrayList<Item>();
	public List<Player> players = new ArrayList<Player>();

	Level level;

	public void update(){
		if (level != null) {
			int startX = (Camera.getX() / level.getTileWidth()) - 1;
			int startY = (Camera.getY() / level.getTileHeight()) - 1;
			for (int y = startY; y < (startY + level.getHeight() + 1); y++) {
				for (int x = startX; x < (startX + level.getHeight() + 1); x++) {
					if (level.getTile(x, y) instanceof AnimatedTile) ((AnimatedTile) level.getTile(x, y)).update();
				}
			}
		}

		for (Player player : players) {
			player.update(this);
		}

		for(Creature creature : creatures){
			creature.update(this);
		}

		for (int i = 0; i < items.size(); i++) {
			Item item =  items.get(i);
			if(item.isPickedUp()) {
				items.remove(i);
				continue;
			}
			for (Player player : players) {
				if (player == null) continue;
				if (player.intersects(item)) {
					item.pickedUpBy(player);
				}
			}
		}

		Camera.update();
	}

	/**
	 * Render all visible entities, Camera movement and tile animations.
	 */
	public void render(Graphics g){
		if (level != null) level.render(g);

		for(Item item : items){
			if(!item.isOnScreen()) continue;
			item.render(g);
		}

		for (Player player : players) {
			if (player != null) player.render(g);
		}

		for(Creature creature : creatures){
			if(!creature.isOnScreen()) continue;
			creature.render(g);
		}

		for(Entity entity : entities){
			if(!entity.isOnScreen()) continue;
			entity.render(g);
		}
	}

	public void add(Entity entity){
		if(entity instanceof Player){
			players.add((Player) entity);
		}else if(entity instanceof Creature){
			creatures.add((Creature)entity);
		}else if(entity instanceof Item){
			items.add((Item)entity);
		}else{
			entities.add(entity);
		}
	}

	public void setLevel(Level level) {
		this.level = level;
	}

	public Level getLevel() {
		return level;
	}

	public String getEntityString(){
		return "World - E:" + entities.size() + " C:" + creatures.size() + " I:" + items.size() + " P:" + players.size();
	}
}
