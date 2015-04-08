package com.filesharing.main;

import com.filesharing.actionManagersImp.FileManagerImp;
import com.filesharing.clientactionsImp.ClientImp;

public class NodeLoop extends Thread {
	ClientImp client;

	public NodeLoop() {
		client = new ClientImp();
	}
	
	public void run() {
		// initialize the music files
		new FileManagerImp().initiateFilesOfTheNode();
		
		// register with bootstrap server
		client.registerAndJoinOverlay();
		
		// start listening to other nodes
		client.listenToNodes();
	}

	public void searchFile(String prefixOfFile) {
		client.searchFile(prefixOfFile);
	}

	public static void main(String[] args) {
		NodeLoop nodeLoop = new NodeLoop();
		nodeLoop.start();
	}
}
