package com.infinimango.flux.world.entity;

import com.infinimango.flux.world.World;

import java.net.InetAddress;

public abstract class Player extends Creature {
	String name;
	InetAddress address;
	int port;

	public Player(float x, float y){
		super(x, y);
	}

	public abstract void update(World world);

	public String getName() {
		return name;
	}
}
