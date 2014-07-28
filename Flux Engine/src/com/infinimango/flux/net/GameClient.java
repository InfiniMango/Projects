package com.infinimango.flux.net;

import com.infinimango.flux.Debug;
import com.infinimango.flux.Display;

import java.io.IOException;
import java.net.*;

public abstract class GameClient extends Thread {
	InetAddress IPAddress;
	DatagramSocket socket;

	int port;

	long pingTimer;

	public GameClient(String IPAddress, int port){
		connectTo(IPAddress, port);
	}

	public GameClient(String IPAddress){
		this(IPAddress, GameServer.DEFAULT_PORT);
	}

	public GameClient(){}

	public void connectTo(String IPAddress, int port){
		this.port = port;
		try {
			this.IPAddress = InetAddress.getByName(IPAddress);
			socket = new DatagramSocket();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (SocketException e) {
			e.printStackTrace();
		}
		start();
	}

	public void disconnect(){
		socket.disconnect();
	}

	public void run() {
		while (Display.isRunning()) {
			if(socket == null) return;
			byte data[] = new byte[1024];
			try {
				DatagramPacket packet = new DatagramPacket(data, data.length);
				socket.receive(packet);

				if(new String(packet.getData()).trim().equalsIgnoreCase("pong")) {
					Debug.cOut("Ping: " + (System.currentTimeMillis() - pingTimer), IPAddress.getHostAddress(), port);
					return;
				}

				Debug.cOut("Received this: " + new String(packet.getData()), IPAddress.getHostAddress(), port);
				parse(data);
			} catch (IOException e) {
				e.printStackTrace();
			}
			update();
		}
	}

	public abstract void update();

	protected abstract void parse(byte[] data);

	public void send(byte data[]){
		DatagramPacket packet = new DatagramPacket(data, data.length, IPAddress, port);
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void send(String msg){
		send(msg.getBytes());
	}

	public void ping(){
		send("ping");
		pingTimer = System.currentTimeMillis();
	}
}
