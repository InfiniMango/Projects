package com.infinimango.flux.world;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import com.infinimango.flux.world.entity.AnimatedEntity;
import com.infinimango.flux.world.entity.AnimatedItem;
import com.infinimango.flux.world.entity.Creature;
import com.infinimango.flux.world.entity.Entity;
import com.infinimango.flux.world.entity.Item;
import com.infinimango.flux.world.entity.Player;

public class World {
	public List<Entity> entities = new ArrayList<Entity>();
	public List<Creature> creatures = new ArrayList<Creature>();
	public List<Item> items = new ArrayList<Item>();
	public List<TileMap> tileMaps = new ArrayList<TileMap>();

	public Player player;

	public void update() {
		// Update tilemaps
		for (TileMap tm : tileMaps) {
			tm.update();
		}

		// Update animated entities
		for (Entity e : entities) {
			if (e instanceof AnimatedEntity)
				((AnimatedEntity) e).updateAnimation();
		}

		// Update creatures
		for (Creature c : creatures) {
			c.update(this);
		}

		// Delete dead creatures
		for (int c = 0; c < creatures.size(); c++) {
			if (creatures.get(c).isDead())
				creatures.remove(c);
		}

		// Update items
		for (Item i : items) {
			if (i instanceof AnimatedItem)
				((AnimatedItem) i).updateAnimation();
		}

		// Delete items that have been picked up
		for (int i = 0; i < items.size(); i++) {
			if (items.get(i).isPickedUp())
				items.remove(i);
		}
	}

	public void render(Graphics g) {
		for (TileMap tm : tileMaps) {
			tm.render(g);
		}

		for (Item item : items) {
			if (!item.isOnScreen())
				continue;
			item.render(g);
		}

		if (player != null)
			player.render(g);

		for (Creature creature : creatures) {
			if (!creature.isOnScreen())
				continue;
			creature.render(g);
		}

		for (Entity entity : entities) {
			if (!entity.isOnScreen())
				continue;
			entity.render(g);
		}
	}

	public void add(Entity entity) {
		entities.add(entity);
	}

	public void add(Creature creature) {
		creatures.add(creature);
	}

	public void add(Item item) {
		items.add(item);
	}

	public void add(Player player) {
		this.player = player;
	}

	public void add(TileMap tileMap) {
		tileMaps.add(tileMap);
	}

	public String getEntityString() {
		return "World - M:" + tileMaps.size() + " E:" + entities.size() + " C:"
				+ creatures.size() + " I:" + items.size() + " P:"
				+ (player != null);
	}

	/**
	 * Sets a new player to the world.
	 * 
	 * @param player
	 *            New player
	 */
	public void setPlayer(Player player) {
		this.player = player;
	}
}
