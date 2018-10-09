/**
 * @author Igor N Faustino, Claudia Sampedro
 * create at octuber 9 2018
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
			byte[] msg = "teste".getBytes();
			DatagramPacket msgOut = new DatagramPacket(msg, msg.length, this.group, 6789);
			this.multicastSocket.send(msgOut);
			Thread t = new Thread(new ReciveRunnable(multicastSocket, running));
			t.start();

			while(t.isAlive()) {
				Thread.sleep(500);
			}
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