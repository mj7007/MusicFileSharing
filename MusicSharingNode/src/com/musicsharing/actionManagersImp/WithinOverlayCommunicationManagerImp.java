package com.musicsharing.actionManagersImp;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.List;

import com.musicsharing.actionManagers.WithinOverlayCommunicationManager;
import com.musicsharing.dtos.TableRecord;
import com.musicsharing.globalitems.RoutingTable;
import com.musicsharing.utils.SocketClient;
import com.musicsharing.utilsImp.Constants;
import com.musicsharing.utilsImp.MessageGenerator;
import com.musicsharing.utilsImp.UDPClient;

public class WithinOverlayCommunicationManagerImp implements WithinOverlayCommunicationManager {

	/*
	 * request: length JOIN IP_address port_no e.g 0027 JOIN 64.12.123.190 432
	 * response: length JOINOK value e.g 0014 JOINOK 0
	 */
	@Override
	public void informTheJoining() {
		String joinMessage = MessageGenerator.createJoinOverlayMessage(Constants.NODE_IP, Constants.NODE_PORT);
		System.out.println("Inform two nodes about joining: " + joinMessage);
		
		SocketClient socketClient = new UDPClient();
		for (Integer key : RoutingTable.getInstance().getRecords().keySet()) {
			try {
				TableRecord record = RoutingTable.getInstance().getRecords().get(key);
				socketClient.callAndGetResponse(record.getServer(), record.getPort(), joinMessage);
				
				System.out.println("Join message sent to: " + record.getServer() + " " + record.getPort());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/*
	 * request: length LEAVE IP_address port_no e.g 0028 LEAVE 64.12.123.190 432
	 * response: length LEAVEOK value e.g 0014 LEAVEOK 0
	 */
	@Override
	public void informTheLeaving() {
		String messageSuffix = "";
		String fullMessage = "";

		messageSuffix += " JOIN " + Constants.NODE_IP + " " + Constants.myServerPort;

		double d = (double) (messageSuffix.length() + 4) / (double) 10000;

		fullMessage += String.format("%.4f", d).substring(2);
		fullMessage += messageSuffix;
		System.out.println(fullMessage);
		SocketClient socketClient=new UDPClient();
		for (Integer key : RoutingTable.getInstance().getRecords()
				.keySet()) {
			try {
				socketClient.callAndGetResponse(
						RoutingTable.getInstance().getRecords()
								.get(key).getServer(),
						RoutingTable.getInstance().getRecords()
								.get(key).getPort(), fullMessage);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	/*
	 * length SER IP port file_name hops e.g 0047 SER 129.82.62.142 5070
	 * "Lord of the rings"
	 */
	@Override
	public void flooodTheMessage(String server, int port, String fileName,
			int TTL) {

		String messageSuffix = "";
		String fullMessage = "";
		if (TTL != 0) {
			TTL--;
			messageSuffix += " SER " + server + " " + port + " " + fileName
					+ " " + TTL;
		}

		double d = (double) (messageSuffix.length() + 4) / (double) 10000;

		fullMessage += String.format("%.4f", d).substring(2);
		fullMessage += messageSuffix;
		System.out.println(fullMessage);
		SocketClient socketClient=new UDPClient();
		for (Integer key : RoutingTable.getInstance().getRecords()
				.keySet()) {
			try {
				socketClient.callAndGetResponse(
						RoutingTable.getInstance().getRecords()
								.get(key).getServer(),
						RoutingTable.getInstance().getRecords()
								.get(key).getPort(), fullMessage);
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	/*
	 * length SEROK no_files IP port hops filename1 filename2 e.g 0114 SEROK 3
	 * 129.82.128.1 2301 baby_go_home.mp3 baby_come_back.mp3 baby.mpeg
	 */
	@Override
	public void responseWithMatchingFiles(String server, int port,
			List<String> files) {
		String messageSuffix = "";
		String fullMessage = "";

		messageSuffix += " SEROK " + files.size() + " " + Constants.NODE_IP
				+ " " + Constants.myServerPort + " " + 0;
		Iterator<String> it = files.iterator();

		while (it.hasNext()) {
			String next = it.next();
			messageSuffix += " " + next;
		}

		double d = (double) (messageSuffix.length() + 4) / (double) 10000;

		fullMessage += String.format("%.4f", d).substring(2);
		fullMessage += messageSuffix;
		System.out.println(fullMessage);
		SocketClient socketClient=new UDPClient();
		if (files.size() != 0) {
			try {
				socketClient.callAndGetResponse(server, port, fullMessage);
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void responseTheLeaving(String server, int port) {
		for (Integer key : RoutingTable.getInstance().getRecords()
				.keySet()) {
			if (RoutingTable.getInstance().getRecords().get(key)
					.getServer().equals(server)
					&& RoutingTable.getInstance().getRecords()
							.get(key).getPort() == port) {

				RoutingTable.getInstance().getRecords()
						.remove(key);
			}

		}

		String fullMessage = "0014 LEAVEOK 0";
		SocketClient socketClient=new UDPClient();
		try {
			socketClient.callAndGetResponse(server, port, fullMessage);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void searchForMusicFile(String prefixOfMusic) {
		String messageSuffix = "";
		String fullMessage = "";

		messageSuffix += " SER "+ Constants.NODE_IP
				+ " " + Constants.myServerPort + " "+prefixOfMusic+" " + Constants.TTL;
		
		double d = (double) (messageSuffix.length() + 4) / (double) 10000;

		fullMessage += String.format("%.4f", d).substring(2);
		fullMessage += messageSuffix;
		System.out.println(fullMessage);
		SocketClient socketClient=new UDPClient();
		for (Integer key : RoutingTable.getInstance().getRecords()
				.keySet()) {
			try {
				socketClient.callAndGetResponse(
						RoutingTable.getInstance().getRecords()
								.get(key).getServer(),
						RoutingTable.getInstance().getRecords()
								.get(key).getPort(), fullMessage);
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

}
