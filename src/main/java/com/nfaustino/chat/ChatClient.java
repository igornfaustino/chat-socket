/**
 * @author Igor N Faustino, Claudia Sampedro
 * create at october 9 2018
 * 
 * Multicast chat client
 * 
 * Multicas Ip -> 255.1.2.3
 * Multicast port -> 6789
 * UDP port -> 6799?
 */

package com.nfaustino.chat;

import java.io.IOException;
import java.net.BindException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.util.ArrayList;

public class ChatClient {
	MulticastSocket multicastSocket;
	DatagramSocket datagramSocket;
	InetAddress group;

	int portMultcast;
	int portUdp;
	
	String username;

	ArrayList<User> usersOnline = new ArrayList<>();

	public ChatClient() {
		this.multicastSocket = null;
		this.datagramSocket = null;

		this.portMultcast = 6789;
		this.portUdp = 6799;

		try {
			this.group = InetAddress.getByName("225.1.2.3");
		} catch (IOException e) {
			System.out.println("IO: " + e.getMessage());
		}

		initializeMulticastSocket();
		initializeDatagramSocket();
		System.out.println("initializing chat....");
	}

	// --------------- GET SET ------------------

	public String getUsername() { return this.username; }
	public void setUsername(String username) { this.username = username; }

	public MulticastSocket getMulticastSocket() { return this.multicastSocket; }
	public DatagramSocket getDatagramSocket() { return this.datagramSocket; }

	public ArrayList<User> getUsersOnline() { return this.usersOnline; }
	public void addUserOnline(User user) { this.usersOnline.add(user); }
	public void deleteUserOnline(User user) { this.usersOnline.remove(user); }

	// ------------------------------------------

	// --------------- INITIALIZE ---------------

	void initializeMulticastSocket() {
		try {
			this.multicastSocket = new MulticastSocket(this.portMultcast);
			this.multicastSocket.joinGroup(this.group); // connect client to the multicast group
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void initializeDatagramSocket() {
		boolean error;
		do {
			error = false;
			try {
				this.datagramSocket = new DatagramSocket(this.portUdp);
			} catch (BindException e) {
				error = true;
				this.portUdp++;
			} catch (SocketException e) {
				e.printStackTrace();
			}
		} while (error);
	}

	// -----------------------------------------

	/**
	 * starts the client
	 */
	void execute() {
		try {
			Thread sendMessage = new Thread(new SendRunnable(this));
			Thread reciveMulticast = new Thread(new ReciveMulticastRunnable(this));
			Thread reciveDatagram = new Thread(new ReciveUdpRunnable(this));

			reciveMulticast.start();
			reciveDatagram.start();
			sendMessage.start();

			while(reciveMulticast.isAlive()) Thread.sleep(500);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (this.multicastSocket != null) this.multicastSocket.close();
		}
	}

	public void sendMulticast(String msg) {
		byte[] msgByte = msg.getBytes();
		DatagramPacket msgOut = new DatagramPacket(msgByte, msgByte.length, this.group, this.portMultcast);

		try {
			this.multicastSocket.send(msgOut);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendDatagram(String msg, InetAddress ip) {
		byte[] msgByte = msg.getBytes();
		DatagramPacket msgOut = new DatagramPacket(msgByte, msgByte.length, ip, this.portUdp);

		try {
			this.datagramSocket.send(msgOut);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendDatagram(String msg, InetAddress ip, int port){
		byte[] msgByte = msg.getBytes();
		DatagramPacket msgOut = new DatagramPacket(msgByte, msgByte.length, ip, port);

		try {
			this.datagramSocket.send(msgOut);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public static void main(String[] args) {
		ChatClient client = new ChatClient();
		client.execute();
	}
}