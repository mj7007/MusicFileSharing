package com.filesharing.clientactions;

public interface Client {
	
	boolean registerAndJoinOverlay();
	
	String serviceTheReceivedMessage(String message);
	
	void searchFile(String prefixOfFile);
	
	void listenToNodes();
	
	void leaveTheOverlay();
	
}
