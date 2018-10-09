/**
 * @author Igor N Faustino, Claudia Sampedro
 * create at october 9 2018
 * 
 * runnable to send messages
 */

 package com.nfaustino.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class SendRunnable implements Runnable {
	MulticastSocket multicastSocket;
	InetAddress groupIp;
	int portMulticast;
	BufferedReader in;
	StringBuilder username;

	public SendRunnable(MulticastSocket multicastSocket, InetAddress groupId, int portMulticast, StringBuilder username) {
		this.multicastSocket = multicastSocket;
		this.groupIp = groupId;
		this.portMulticast = portMulticast;
		this.in = new BufferedReader(new InputStreamReader(System.in));
		this.username = username;
	}
	
	private void sendMulticast(String msg) {
		byte[] msgByte = msg.getBytes();
		DatagramPacket msgOut = new DatagramPacket(msgByte, msgByte.length, this.groupIp, 6789);

		try {
			this.multicastSocket.send(msgOut);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		System.out.println("Type yout username:");
		try {
			this.username.append(in.readLine().trim());
			sendMulticast("JOIN " + this.username.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
 }