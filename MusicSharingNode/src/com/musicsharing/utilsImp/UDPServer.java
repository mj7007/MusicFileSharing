package com.musicsharing.utilsImp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.UnknownHostException;

import com.musicsharing.utils.SocketServer;

public class UDPServer implements SocketServer {

	@Override
	public String listenAndGetResponse(String server, int portNumber, String message) throws SocketException, UnknownHostException, IOException {
		// Create a socket to listen on the port.
	    DatagramSocket socket = new DatagramSocket(portNumber);
	    
	    // Create a buffer to read datagrams into. If a
	    // packet is larger than this buffer, the
	    // excess will simply be discarded!
	    byte[] buffer = new byte[2048];
	    
	    // Create a packet to receive data into the buffer
	    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
	    
	    // Now loop forever, waiting to receive packets and printing them.
	    while (true) {
	    	// Wait to receive a datagram
	    	socket.receive(packet);
	    	
	    	// Convert the contents to a string, and display them
	    	String msg = new String(buffer, 0, packet.getLength());
	        System.out.println("Message Received from " + packet.getAddress().getHostName() + " : " + msg);  
	        
	        // Reset the length of the packet before reusing it.
	        packet.setLength(buffer.length);
	    }
	    
        // Convert the contents to a string, and display them
        
	}

}
