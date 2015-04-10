package com.filesharing.clientactions;

public interface Client {
	
	boolean registerWithBSServer();
	
	boolean unregisterFromBServer();
	
	void joinTheOverlay();
	
	void leaveTheOverlay();
	
	void stopListeningToNodes();
	
	void listenToNodes();
	
	String serviceTheReceivedMessage(String message);
	
	void searchFile(String prefixOfFile);
	
}
