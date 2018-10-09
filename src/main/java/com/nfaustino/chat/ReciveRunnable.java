/**
 * @author Igor N Faustino, Claudia Sampedro
 * Create at october 9 2018
 * 
 * Runnable to recive all msgs
 */

package com.nfaustino.chat;

import java.net.DatagramPacket;
import java.net.MulticastSocket;

public class ReciveRunnable implements Runnable {
	MulticastSocket multicastSocket;
	StringBuilder username;

	public ReciveRunnable(MulticastSocket multicastSocket, StringBuilder username) {
			this.multicastSocket = multicastSocket;
			this.username = username;
	}
	
	public void run() {
		try {
			while(true){
				byte[] buffer = new byte[1000];
				DatagramPacket msgIn = new DatagramPacket(buffer, buffer.length);
				multicastSocket.receive(msgIn);

				String msgRecive = new String(msgIn.getData());
				String[] cmdToken = msgRecive.split(" ");
				if (cmdToken[0].equals("JOIN")) {
					String newUser = "";
					for(int i = 1; i < cmdToken.length; i++){
						newUser = newUser.concat(cmdToken[i] + " ");
					}
					newUser = newUser.trim();
					if(!newUser.equals(username.toString())){
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