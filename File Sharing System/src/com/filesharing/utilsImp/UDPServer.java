package com.filesharing.utilsImp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.UnknownHostException;

import com.filesharing.clientactions.Client;
import com.filesharing.utils.SocketServer;

public class UDPServer implements SocketServer {

	private static boolean FLAG_LISTEN = false;
	
	private Client client;
	private  DatagramSocket socket;
	
	public UDPServer(Client client) {
		this.client = client;
	}
	
	@Override
	public String listenAndGetResponse(String server, int portNumber, String message) throws SocketException, UnknownHostException, IOException {
		// Create a socket to listen on the port.
	   socket = new DatagramSocket(portNumber);
	    
	    // Create a buffer to read datagrams into. If a
	    // packet is larger than this buffer, the
	    // excess will simply be discarded!
	    byte[] buffer = new byte[2048];
	    
	    // Create a packet to receive data into the buffer
	    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
	    
	    // start listening
	    FLAG_LISTEN = true;
	    
	    // Now loop forever, waiting to receive packets and printing them.\
	    while (FLAG_LISTEN) {
	    	// Wait to receive a datagram
	    	socket.receive(packet);
	    	
	    	// Convert the contents to a string, and display them
	    	String msg = new String(buffer, 0, packet.getLength());
	        System.out.println("Message Received from " + packet.getAddress().getHostName() + " : " + msg);  
	        
	        // service the received message
		    client.serviceTheReceivedMessage(msg);
		    
		    // Reset the length of the packet before reusing it.
	        packet.setLength(buffer.length);
	    }
	    
	    // close the socket
	    if (!socket.isClosed()) {
	    	socket.close();
		}
	    
	    return null;
	}
	
	@Override
	public void stopListening() {
		socket.close();
		FLAG_LISTEN = false;
	}

}
