/**
 * @author Igor N Faustino, Claudia Sampedro
 * create at october 9 2018
 * 
 * Util methods
 */

package com.nfaustino.chat;

public class Util {
	static public String extractUsername(String str) {
		return str.replaceAll("(.*\\[)|(\\].*)", "");
	}
}