package com.filesharing.utilsImp;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Vector;

import org.apache.xmlrpc.XmlRpcClient;

import com.filesharing.utils.RPCClient;

public class RPCClientImp implements RPCClient {

	@Override
	public String callAndGetResponse(String server, int portNumber, String message) throws SocketException, UnknownHostException, IOException {
		try {
			XmlRpcClient client = new XmlRpcClient("http://"+server+":"+portNumber+"/RPC2");
			//XmlRpcClient client = new XmlRpcClient(server, portNumber);
			Vector<String> params = new Vector<String>();

			params.addElement(new String(message));

			Object result = client.execute("sample.serviceMessage", params);

			System.out.println("The result is: " + result.toString());

		} catch (Exception exception) {
			System.err.println("JavaClient: " + exception);
		}
		return null;
	}

}
