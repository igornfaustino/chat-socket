/**
 * @author Igor N Faustino, Claudia Sampedro
 * create at october 9 2018
 * 
 * runnable to send messages
 */

 package com.nfaustino.chat;

import java.io.Console;
import java.util.ArrayList;

public class SendRunnable implements Runnable {
	Console in;
	ChatClient chatClient;

	public SendRunnable(ChatClient chatClient) {
		this.chatClient = chatClient;
		this.in = System.console();
	}

	String readLine() {
		String line = "";
		try {
			line = in.readLine().trim();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return line;
	}

	String readLine(String msg) {
		String line = "";
		try {
			line = in.readLine(msg).trim();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return line;
	}

	void printHelp() {
		System.out.println("Commands");
		System.out.println("help: print help");
		System.out.println("list users: list all online users");
	}

	void mainLoop() {
		System.out.println("you enter in the chat room... type help to see all the commands");
		while(true) {
			String line = readLine("\n>>> ").trim();
			
			String[] cmdTokens = line.split(" ");
			if (line.toLowerCase().equals("help")){
				printHelp();
			} else if (line.toLowerCase().equals("list users")) {
				listUsers();
			} else if (cmdTokens[0].equalsIgnoreCase("msg")) {
				sendMsg(line);
			}
		}
	}

	void listUsers() {
		ArrayList<User> users = this.chatClient.getUsersOnline();
		if(users.isEmpty()){
			System.out.println("No other user connected");
			return;
		}

		for (User user : users) {
			System.out.println("- " + user.getUsername());
		}
	}

	void sendMsg(String rawMsg) {
		String text = rawMsg.toLowerCase().replace("msg", "").trim();
		String msg = "MSG [" + this.chatClient.getUsername() + "] \"" +  text + "\"";

		this.chatClient.sendMulticast(msg);
	}

	public void run() {
		chatClient.setUsername(readLine("\nType yout username: "));
		chatClient.sendMulticast("JOIN " + chatClient.portUdp + " [" + chatClient.getUsername() + "]");
		
		mainLoop();
	}
 }