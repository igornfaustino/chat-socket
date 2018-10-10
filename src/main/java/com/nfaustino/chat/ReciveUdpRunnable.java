/**
 * @author Igor N Faustino, Claudia Sampedro
 * create at october 9 2018
 * 
 * Runnable to recive UDP datagrams
 */

 package com.nfaustino.chat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.Socket;

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
				} else if (cmdToken[0].equals("DOWNFILE")) {
					initializeFile();
					String filename = msg.replaceAll("^.*\\]", "").trim();
					if(file.fileExist(filename)){
						int filesize = file.getFile(filename).length;
						this.chatClient.sendDatagram("DOWNINFO [" + filename + "," + filesize + "," + InetAddress.getLocalHost().getHostAddress() + "," + this.chatClient.portTcp + "]", recivPacket.getAddress(), recivPacket.getPort());
					}
				} else if (cmdToken[0].equals("DOWNINFO")){
					initializeFile();
					String content = Util.extractUsername(msg);
					String[] contentList = content.split(",");

					new Thread(new ClientTcpRunnable(
						contentList[0], 
						Integer.valueOf(contentList[1]).intValue(), 
						contentList[2], 
						Integer.valueOf(contentList[3]).intValue(),
						file	
					)).start();
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


class ClientTcpRunnable implements Runnable {
	Socket socket;
	String filename;
	int size;

	DataInputStream in;
	DataOutputStream out;

	MyFile file;

	public ClientTcpRunnable(String filename, int size, String address, int port, MyFile file){
		this.socket = null;
		this.filename = filename;
		this.size = size;
		this.file = file;
		try {
			this.socket = new Socket(InetAddress.getByName(address), port);
			in = new DataInputStream(this.socket.getInputStream());
			out = new DataOutputStream(this.socket.getOutputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void run() {
		try {
			out.writeUTF(this.filename);
			byte[] fileByte = new byte[this.size];
			for(int i = 0; i < fileByte.length; i++){
				fileByte[i] = in.readByte();
			}
			file.saveFile(filename, fileByte);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}