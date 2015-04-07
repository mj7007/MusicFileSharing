package com.musicsharing.utilsImp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import com.musicsharing.utils.SocketClient;

public class UDPClient implements SocketClient {

	@Override
	public String callAndGetResponse(String destinationIP, int destinationPort, String message) throws IOException {
		DatagramSocket socket = new DatagramSocket(Constants.NODE_UDP_PORT);
		InetAddress destinationInetAddress = InetAddress.getByName(destinationIP);
		
		DatagramPacket packet = new DatagramPacket(message.getBytes(), message.length(), destinationInetAddress, destinationPort);
		socket.send(packet);
		socket.close();

		return message;
	}
	
}