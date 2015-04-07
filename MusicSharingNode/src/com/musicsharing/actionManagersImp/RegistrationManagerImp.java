package com.musicsharing.actionManagersImp;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

import com.musicsharing.actionManagers.RegistrationManager;
import com.musicsharing.utils.SocketClient;
import com.musicsharing.utilsImp.MessageGenerator;
import com.musicsharing.utilsImp.SocketClientImp;

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

}
