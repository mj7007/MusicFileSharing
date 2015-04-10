package com.filesharing.utils;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.rmi.Remote;

public interface RPCClient extends Remote {
	
	String callAndGetResponse(String server,int portNumber,String message) throws SocketException, UnknownHostException, IOException;

}
