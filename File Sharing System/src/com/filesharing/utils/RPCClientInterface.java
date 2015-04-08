package com.filesharing.utils;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.rmi.Remote;
import java.util.Vector;

import org.apache.xmlrpc.XmlRpcClient;

public interface RPCClientInterface extends Remote {
	String callAndGetResponse(String server,int portNumber,String message) throws SocketException, UnknownHostException, IOException;

}
