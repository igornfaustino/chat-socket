/**
 * @author Igor N Faustino, Claudia Sampedro
 * Create at october 9 2018
 * 
 * User class to store all data related to users
 */

 package com.nfaustino.chat;

import java.net.InetAddress;

public class User {
	String username;
	InetAddress ip;
	int portUdp;

	public User(String username, InetAddress ip, int portUdp) {
		this.username = username;
		this.ip = ip;
		this.portUdp = portUdp;
	}

	// -------------- GET SET ---------------------

	public String getUsername() { return this.username; }

	public InetAddress getIp() { return this.ip; }

	public int getPortUdp() { return this.portUdp; }
	
	// --------------------------------------------
 }