/**
 * @author Igor N Faustino, Claudia Sampedro
 * Create at october 9 2018
 * 
 * Runnable to recive all msgs
 */

package com.nfaustino.chat;

import java.net.DatagramPacket;

public class ReciveMulticastRunnable implements Runnable {
	ChatClient chatClient;

	public ReciveMulticastRunnable(ChatClient chatClient) {
			this.chatClient = chatClient;
	}
	
	public void run() {
		try {
			while(true){
				byte[] buffer = new byte[1000];
				DatagramPacket msgIn = new DatagramPacket(buffer, buffer.length);
				chatClient.getMulticastSocket().receive(msgIn);

				String msgRecive = new String(msgIn.getData());
				String[] cmdToken = msgRecive.split(" ");

				// Test if command is a join
				if (cmdToken[0].equals("JOIN")) {
					String newUser = "";
					for(int i = 1; i < cmdToken.length; i++){
						newUser = newUser.concat(cmdToken[i] + " ");
					}
					newUser = newUser.trim();
					
					// test if msg is not your only username
					if(!newUser.equals(chatClient.getUsername())){
						System.out.println(newUser);
					}
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
	}
}