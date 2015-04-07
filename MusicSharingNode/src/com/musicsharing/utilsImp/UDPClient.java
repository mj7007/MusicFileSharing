package com.musicsharing.utilsImp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import com.musicsharing.utils.SocketClient;

public class UDPClient implements SocketClient {

	@Override
	public String callAndGetResponse(String server, int portNumber,String message) throws IOException {
		DatagramSocket ds = new DatagramSocket();
		InetAddress ip = InetAddress.getLocalHost();

		DatagramPacket dp = new DatagramPacket(message.getBytes(), message.length(), ip, 2000);
		ds.send(dp);
		ds.close();

		return message;
	}
	
}