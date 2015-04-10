package com.filesharing.utilsImp;

import org.apache.xmlrpc.WebServer;

import com.filesharing.clientactionsImp.ClientImp;
import com.filesharing.utils.RPCServerInterface;


public class RPCServer implements RPCServerInterface {
	
	public String serviceMessage(String msg){
		System.out.println("RPC Server says:" + msg);
		return new ClientImp().serviceTheReceivedMessage(msg);
	}

	@Override
	public void startWebServer() {
		try {
	         System.out.println("Attempting to start RPC Server...");
	         
	         // start the web server
	         WebServer server = new WebServer(Constants.NODE_PORT);
	 		 server.addHandler("sample", new RPCServer());
	         server.start();
	         
	         System.out.println("Started successfully.");
	         System.out.println("Accepting requests. (Halt program to stop.)");
	         
	      } catch (Exception exception){
	         System.err.println("JavaServer: " + exception);
	      }
	}
	
}
