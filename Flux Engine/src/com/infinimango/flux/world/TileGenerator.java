package com.infinimango.flux.world;

import com.infinimango.flux.world.tile.Tile;

public abstract class TileGenerator {
	public abstract void generate(int data[][]);
	public abstract void generate(Tile tiles[][]);
}
