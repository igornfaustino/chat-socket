/**
 * @author Igor N Faustino, Claudia Sampedro
 * create at october 9 2018
 * 
 * Runnable to recive UDP datagrams
 */

 package com.nfaustino.chat;

import java.net.DatagramPacket;

public class ReciveUdpRunnable implements Runnable {
	ChatClient chatClient;

	public ReciveUdpRunnable(ChatClient chatClient) {
		this.chatClient = chatClient;
	}
	
	public void run() {
		while(true) {
			byte[] buffer = new byte[1000];
			DatagramPacket recivPacket = new DatagramPacket(buffer, buffer.length);
			try {
				this.chatClient.getDatagramSocket().receive(recivPacket);
				String msg = new String(recivPacket.getData());
				String[] cmdToken = msg.trim().split(" ");

				if (cmdToken[0].equals("JOINACK")) {
					String newUser = Util.extractUsername(msg);
					newUser = newUser.trim();

					chatClient.addUserOnline(new User(newUser,
											 recivPacket.getAddress(),
											 recivPacket.getPort()));
				} else if (cmdToken[0].equals("MSGIDV")){
					System.out.println(msg);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
 }