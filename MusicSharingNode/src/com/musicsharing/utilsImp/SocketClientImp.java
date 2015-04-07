package com.musicsharing.utilsImp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import com.musicsharing.utils.SocketClient;

public class SocketClientImp implements SocketClient {

	@Override
	public String callAndGetResponse(String server, int port, String message) {
		Socket socket = null;
		try {
			socket = new Socket(server, port);
			
			InputStream in = socket.getInputStream();
			OutputStream out = socket.getOutputStream();
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			PrintWriter writer = new PrintWriter(out);
			
			// write the message to socket
			writer.write(message);
			writer.flush();
			
			// read the input
			int input;
			StringBuilder builder = new StringBuilder();
			while ((input = reader.read()) != -1) {
				builder.append((char) input);
			}
			
			in.close();
			out.close();

			System.out.println("Server says:" + builder.toString());

			return builder.toString();
		} catch (IOException exp) {
			exp.printStackTrace();
			System.out.println(exp.getLocalizedMessage());
			System.exit(1);
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return null;
	}

}
