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
	MyFile file = null;

	public ReciveUdpRunnable(ChatClient chatClient) {
		this.chatClient = chatClient;
	}
	
	public void run() {
		while(this.chatClient.isRunning()) {
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
					System.out.println("\n- " + msg);
				} else if (cmdToken[0].equals("LISTFILES")){
					initializeFile();

					String msgFile = "FILES [";
					if(file.getFiles().length > 0){
						for(String file : file.getFiles()){
							msgFile += file + ",";
						}
						msgFile = msgFile.replaceFirst(",$", "]");
					} else {
						msgFile += "]";
					}
					this.chatClient.sendDatagram(msgFile, recivPacket.getAddress(), recivPacket.getPort());
				} else if (cmdToken[0].equals("FILES")){
					String files = Util.extractUsername(msg);
					String[] listFiles = files.split(",");
					if(!files.isEmpty()){
						for (String file : listFiles){
							System.out.println("- " + file);
						}
					} else {
						System.out.println("No files avaliable");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	void initializeFile() {
		if (this.file == null) {
			this.file = new MyFile("/home/igornfaustino/Documents/code/faculdade/6_periodo/sd/chat-socket/data/" + this.chatClient.username);
		}
	}
 }