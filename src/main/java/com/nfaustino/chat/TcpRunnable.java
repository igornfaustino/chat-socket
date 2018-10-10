/**
 * @author Igor N Faustino
 * create at october 10 2018
 * 
 * tcp runnable
 */

package com.nfaustino.chat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class TcpRunnable implements Runnable {
	ChatClient chatClient;

	public TcpRunnable(ChatClient chatClient) {
		this.chatClient = chatClient;
	}
	
	public void run() {
		while(this.chatClient.isRunning()){
			Socket clientCon = null;
			try {
				clientCon = this.chatClient.getServerSocket().accept();
			} catch (Exception e) {
				e.printStackTrace();
			}
			new Thread(new ConnTcpRunnable(this.chatClient, clientCon)).start();
		}
	}
}

class ConnTcpRunnable implements Runnable {
	ChatClient chatClient;
	Socket socket;
	DataInputStream in;
	DataOutputStream out;

	public ConnTcpRunnable(ChatClient chatClient, Socket socket) {
		this.chatClient = chatClient;
		this.socket = socket;
		try {
			this.in = new DataInputStream(this.socket.getInputStream());
			this.out = new DataOutputStream(this.socket.getOutputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		try {
			String filename = in.readUTF();
			MyFile file = new MyFile("/home/igornfaustino/Documents/code/faculdade/6_periodo/sd/chat-socket/data/" + this.chatClient.getUsername());
			if (file.fileExist(filename)){
				byte[] fileByte = file.getFile(filename);
				for(int i = 0; i < fileByte.length; i++){
					out.writeByte(fileByte[i]);
				}
			} else {
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				socket.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}