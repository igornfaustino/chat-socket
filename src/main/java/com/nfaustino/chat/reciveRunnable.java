/**
 * @author Igor N Faustino, Claudia Sampedro
 * Create at octuber 9 2018
 * 
 * Runnable to recive all msgs
 */

package com.nfaustino.chat;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class reciveRunnable implements Runnable {
	MulticastSocket multicastSocket;
	Boolean running;

	public reciveRunnable(MulticastSocket multicastSocket, Boolean running) {
			this.multicastSocket = multicastSocket;
			this.running = running;
	}
	
	public void run() {
		byte[] buffer = new byte[1000];
		try {
			while(true){
				DatagramPacket msgIn = new DatagramPacket(buffer, buffer.length);
				multicastSocket.receive(msgIn);
				System.out.println(new String(msgIn.getData()));
				running = Boolean.FALSE;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
	}
}