package com.infinimango.game;

public class Point3D {
	int x, y, z;

	public Point3D(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public int getHexDistanceTo(Point3D point) {
		int xd = Math.abs(x - point.getX());
		int yd = Math.abs(y - point.getY());
		int zd = Math.abs(z - point.getZ());

		if (xd >= yd && xd >= zd)
			return xd;
		if (yd >= xd && yd >= zd)
			return yd;
		if (zd >= xd && zd >= yd)
			return zd;

		return 0;
	}

	public boolean matches(Point3D point) {
		return point.getX() == x && point.getY() == y && point.getZ() == z;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}
}
