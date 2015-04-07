package com.musicsharing.actionManagersImp;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

import com.musicsharing.actionManagers.RegistrationManager;
import com.musicsharing.utils.SocketClient;
import com.musicsharing.utilsImp.SocketClientImp;

public class RegistrationManagerImp implements RegistrationManager {
	SocketClient clientSocket;

	@Override
	public String registerRequestAndGetResponse(String serverIP, int serverPort, String myNodeIP, int myNodePort, String myNodeUsername) {
		clientSocket = new SocketClientImp();

		String message = createRegMessage(myNodeIP, myNodePort, myNodeUsername);

		try {
			return clientSocket.callAndGetResponse(serverIP, serverPort, message);
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;

	}

	private String createRegMessage(String myServer, int myPort, String myUserName) {
		String messageSuffix = " REG " + myServer + " " + myPort + " " + myUserName;
		double length = (double) (messageSuffix.length() + 4) / (double) 10000;

		String fullMessage = String.format("%.4f", length).substring(2) + messageSuffix;
		
		System.out.println("BS Registration Message: " + fullMessage);
		return fullMessage;
	}

}
