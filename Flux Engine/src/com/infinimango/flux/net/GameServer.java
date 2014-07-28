package com.infinimango.flux.net;


import com.infinimango.flux.Debug;
import com.infinimango.flux.Display;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

 public abstract class GameServer extends Thread {
	DatagramSocket socket;
	int port;

	public static final int DEFAULT_PORT = 6529;

	List<Client> connectedClients = new ArrayList<Client>();

	 protected class Client {
		InetAddress IPAddress;
		int port;

		public Client(InetAddress IPAddress, int port){
			this.IPAddress = IPAddress;
			this.port = port;
		}
	}

	public GameServer(int port){
		this.port = port;
		try {
			socket = new DatagramSocket(port);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		start();
	}

	public GameServer(){
		this(DEFAULT_PORT);
	}

	public void run() {
		while (Display.isRunning()) {
			try {
				byte data[] = new byte[1024];
				DatagramPacket packet = new DatagramPacket(data, data.length);

				socket.receive(packet);

				if(new String(packet.getData()).trim().equalsIgnoreCase("ping")) {
					send("pong", packet.getAddress(), packet.getPort());
					return;
				}

				Debug.sOut("Received this: " + new String(packet.getData()), port);

				parse(packet.getData());

			} catch (IOException e) {
				e.printStackTrace();
			}
			update();
		}
		try {
			join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public abstract void update();

	protected abstract void parse(byte[] data);

	public void send(byte data[], InetAddress IPAddress, int port){
		DatagramPacket packet = new DatagramPacket(data, data.length, IPAddress, port);
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void send(String msg, InetAddress IPAddress, int port){
		send(msg.getBytes(), IPAddress, port);
	}

	public void send(Client client, byte[] data){
		send(data, client.IPAddress, client.port);
	}

	 public void send(Client client, String msg){
		 send(msg.getBytes(), client.IPAddress, client.port);
	 }

	public void sendToAll(byte[] data){
		for(Client client : connectedClients){
			send(client, data);
		}
	}
}
