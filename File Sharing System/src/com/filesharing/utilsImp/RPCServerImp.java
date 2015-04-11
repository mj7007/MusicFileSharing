package com.filesharing.utilsImp;

import org.apache.xmlrpc.WebServer;

import com.filesharing.clientactionsImp.ClientImp;
import com.filesharing.utils.Constants;
import com.filesharing.utils.RPCServer;

public class RPCServerImp implements RPCServer {

	private WebServer webServer;

	public String serviceMessage(String msg) {
		System.out.println("RPC Server says : " + msg);
		new ClientImp().serviceTheReceivedMessage(msg);
		return "OK";
	}

	@Override
	public void startWebServer() {
		try {
			System.out.println("Attempting to start RPC Server...");

			if (webServer == null) {
				webServer = new WebServer(Constants.NODE_PORT);
				webServer.addHandler("sample", new RPCServerImp());
			}
			// start the web server
			webServer.start();

			System.out.println("Started successfully");
			System.out.println("Accepting requests...");

		} catch (Exception exception) {
			System.err.println("JavaServer: " + exception);
		}
	}

	@Override
	public void stopWebServer() {
		webServer.shutdown();
	}

}
