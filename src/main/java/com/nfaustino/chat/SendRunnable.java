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
import java.net.InetAddress;
import java.net.MulticastSocket;

public class SendRunnable implements Runnable {
	MulticastSocket multicastSocket;
	InetAddress groupIp;
	int portMulticast;
	BufferedReader in;
	StringBuilder username;
	ChatClient chatClient;

	public SendRunnable(ChatClient chatClient) {
		this.chatClient = chatClient;
		this.in = new BufferedReader(new InputStreamReader(System.in));
	}

	public void run() {
		System.out.println("Type yout username:");
		try {
			chatClient.setUsername(in.readLine().trim());
			chatClient.sendMulticast("JOIN " + chatClient.getUsername());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
 }