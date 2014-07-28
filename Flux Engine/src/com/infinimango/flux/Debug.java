package com.infinimango.flux;

public class Debug {
	public static void out(String s){
		System.out.println("[FLUX] - " + s);
	}

	public static void print(String s){
		System.out.println(s);
	}

	public static void error(String s){
		System.err.print("\n[FLUX] ERROR - " + s + "\n");
	}

	// Client out
	public static void cOut(String msg, String IPAddress, int port){
		String address = IPAddress;
		if(IPAddress.trim().equalsIgnoreCase("127.0.0.1")) address = "localhost";
		System.out.println("[CLIENT - " + address + ":" + port + "] - " + msg);
	}

	// Server out
	public static void sOut(String msg, int port){
		System.out.println("[SERVER - " + port + "] - " + msg);
	}
}
