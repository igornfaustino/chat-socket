/**
 * @author Igor N Faustino, Claudia Sampedro
 * create at october 9 2018
 * 
 * Multicast chat client
 */

package com.nfaustino.chat;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class ChatClient {
	MulticastSocket multicastSocket;
	InetAddress group;
	int portMultcast;
	Boolean running = Boolean.TRUE;
	StringBuilder username = new StringBuilder();

	public ChatClient() {
		this.multicastSocket = null;
		this.portMultcast = 6789;
		try {
			this.group = InetAddress.getByName("225.1.2.3");
		} catch (IOException e) {
			System.out.println("IO: " + e.getMessage());
		}
		System.out.println("initializing chat....");
	}

	/**
	 * starts the client
	 */
	void execute() {
		try {
			this.multicastSocket = new MulticastSocket(this.portMultcast);
			this.multicastSocket.joinGroup(this.group); // connect client to the multicast group
			Thread sendMessage = new Thread(new SendRunnable(this.multicastSocket, this.group, this.portMultcast, this.username));
			Thread t = new Thread(new ReciveRunnable(this.multicastSocket, this.username));

			t.start();
			sendMessage.start();

			while(t.isAlive()) Thread.sleep(500);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (this.multicastSocket != null) this.multicastSocket.close();
			System.out.println(running);
		}
	}

	public static void main(String[] args) {
		ChatClient client = new ChatClient();
		client.execute();
	}
}