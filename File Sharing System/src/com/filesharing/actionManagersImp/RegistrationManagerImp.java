package com.filesharing.actionManagersImp;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

import com.filesharing.actionManagers.RegistrationManager;
import com.filesharing.utils.MessageGenerator;
import com.filesharing.utils.SocketClient;
import com.filesharing.utilsImp.SocketClientImp;

public class RegistrationManagerImp implements RegistrationManager {
	SocketClient clientSocket;

	@Override
	public String registerRequestAndGetResponse(String serverIP, int serverPort, String myNodeIP, int myNodePort, String myNodeUsername) {
		clientSocket = new SocketClientImp();

		String message = MessageGenerator.createRegistrationMessage(myNodeIP, myNodePort, myNodeUsername);
		System.out.println("BS Registration Message: " + message);

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

	@Override
	public String unregisterRequestAndGetResponse(String serverIP, int serverPort, String myNodeIP, int myNodePort, String myNodeUsername) {
		clientSocket = new SocketClientImp();

		String message = MessageGenerator.createUnregistrationMessage(myNodeIP, myNodePort, myNodeUsername);
		System.out.println("BS unregistration Message: " + message);

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

}
