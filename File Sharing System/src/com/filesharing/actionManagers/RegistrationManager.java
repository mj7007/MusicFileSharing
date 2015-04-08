package com.filesharing.actionManagers;

public interface RegistrationManager {
	
	String registerRequestAndGetResponse(String server, int portNumber, String myServer, int myPort, String myUserName);

}
