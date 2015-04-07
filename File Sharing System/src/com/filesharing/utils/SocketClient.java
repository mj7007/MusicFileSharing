package com.filesharing.utils;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

public interface SocketClient {
	
	String callAndGetResponse(String server,int portNumber,String message) throws SocketException, UnknownHostException, IOException;

}
