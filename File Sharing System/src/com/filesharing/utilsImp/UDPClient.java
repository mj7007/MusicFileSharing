package com.filesharing.utilsImp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import com.filesharing.utils.SocketClient;

public class UDPClient implements SocketClient {

	@Override
	public String callAndGetResponse(String destinationIP, int destinationPort, String message) throws IOException {
		DatagramSocket socket = new DatagramSocket();
		InetAddress destinationInetAddress = InetAddress.getByName(destinationIP);
		
		DatagramPacket packet = new DatagramPacket(message.getBytes(), message.length(), destinationInetAddress, destinationPort);
		socket.send(packet);
		socket.close();

		return message;
	}
	
}