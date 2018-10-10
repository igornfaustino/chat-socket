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
		System.out.println("msg text: send the text to everyone in the chat");
	}

	void mainLoop() {
		System.out.println("you enter in the chat room... type help to see all the commands");
		while(this.chatClient.isRunning()) {
			String line = readLine("\n>>> ").trim();
			
			String[] cmdTokens = line.split(" ");
			if (line.toLowerCase().equals("help")){
				printHelp();
			} else if (line.toLowerCase().equals("list users")) {
				listUsers();
			} else if (cmdTokens[0].equalsIgnoreCase("msg")) {
				sendMsg(line);
			} else if (cmdTokens[0].equalsIgnoreCase("msgidv")) {
				sendMsgIdv(line);
			}
			try {
				Thread.sleep(300);
			} catch (Exception e) {
				//TODO: handle exception
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
		String text = rawMsg.toLowerCase().replaceAll("^msg", "").trim();
		String msg = "MSG [" + this.chatClient.getUsername() + "] \"" +  text + "\"";

		this.chatClient.sendMulticast(msg);
	}

	void sendMsgIdv(String rawMsg) {
		String text = rawMsg.toLowerCase().replaceAll("^.*\\]", "").trim();
		String username = Util.extractUsername(rawMsg);

		User userTo = null;
		for(User user: this.chatClient.getUsersOnline()){
			if (user.getUsername().equalsIgnoreCase(username)){
				userTo = user;
				break;
			}
		}
		if (userTo != null) { 
			this.chatClient.sendDatagram("MSGIDV FROM [" + this.chatClient.getUsername() + "] TO [" + userTo.getUsername() + "] \"" + text + "\"", userTo.getIp(), userTo.getPortUdp());
		} else {
			System.out.println("User is not online");
		}
	}

	public void run() {
		chatClient.setUsername(readLine("\nType yout username: "));
		chatClient.sendMulticast("JOIN " + chatClient.portUdp + " [" + chatClient.getUsername() + "]");
		
		mainLoop();
	}
 }