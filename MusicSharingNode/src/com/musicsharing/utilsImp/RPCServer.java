package com.musicsharing.utilsImp;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.apache.xmlrpc.WebServer;

import com.musicsharing.clientactionsImp.ClientImp;
import com.musicsharing.utils.RPCServerInterface;

public class RPCServer implements RPCServerInterface{


	public void serviceMessage(String msg){
		new ClientImp().serviceTheReceivedMessage(msg);
		
	}
	
	public void startWebServer(){
		
		
		try {

	         System.out.println("Attempting to start XML-RPC Server...");
	         
	         WebServer server = new WebServer(Constants.NODE_UDP_PORT);
	         server.addHandler("sample", new RPCServer());
	         server.start();
	         
	         System.out.println("Started successfully.");
	         System.out.println("Accepting requests. (Halt program to stop.)");
	         
	      } catch (Exception exception){
	         System.err.println("JavaServer: " + exception);
	      }
	}



	

}
