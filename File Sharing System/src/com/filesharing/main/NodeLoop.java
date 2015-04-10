package com.filesharing.main;

import com.filesharing.actionManagers.FileManager;
import com.filesharing.actionManagersImp.FileManagerImp;
import com.filesharing.clientactions.Client;
import com.filesharing.clientactionsImp.ClientImp;

public class NodeLoop extends Thread {
	
	private FileManager fileManager;
	private Client client;

	public NodeLoop() {
		fileManager = new FileManagerImp();
		client = new ClientImp();
	}
	
	public void run() {
		// initialize the music files
		fileManager.initiateFilesOfTheNode();
		
		// register with bootstrap server
		boolean isConnected = client.registerWithBSServer();
		
		if (isConnected) {
			// join overlay - UDP
			client.joinTheOverlay();
						
			// start listening to other nodes
			client.listenToNodes();
			
//			// join overlay - RPC
//			client.joinTheOverlay();
		}
		
	}
	
	public void leave() {
		client.leaveTheOverlay();
	}

	public void searchFile(String query) {
		client.searchFile(query);
	}

}
