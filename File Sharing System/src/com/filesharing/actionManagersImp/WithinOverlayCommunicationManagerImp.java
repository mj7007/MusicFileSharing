package com.filesharing.actionManagersImp;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.List;

import com.filesharing.actionManagers.WithinOverlayCommunicationManager;
import com.filesharing.dtos.TableRecord;
import com.filesharing.globalitems.RoutingTable;
import com.filesharing.utils.RPCClientInterface;
import com.filesharing.utils.SocketClient;
import com.filesharing.utilsImp.Constants;
import com.filesharing.utilsImp.MessageGenerator;
import com.filesharing.utilsImp.RPCClient;
import com.filesharing.utilsImp.UDPClient;

public class WithinOverlayCommunicationManagerImp implements WithinOverlayCommunicationManager {

	/*
	 * request: length JOIN IP_address port_no e.g 0027 JOIN 64.12.123.190 432
	 * response: length JOINOK value e.g 0014 JOINOK 0
	 */
	@Override
	public void informTheJoining() {
		String joinMessage = MessageGenerator.createJoinOverlayMessage(Constants.NODE_IP, Constants.NODE_PORT);
		if (RoutingTable.getInstance().getRecords().size() > 0) {
			System.out.println("Inform peers about joining...");
		}
		
		//SocketClient socketClient = new UDPClient();
		RPCClientInterface socketClient=new RPCClient();
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
		String leaveMessage = MessageGenerator.createJoinOverlayMessage(Constants.NODE_IP, Constants.NODE_PORT);
		
		//SocketClient socketClient=new UDPClient();
		RPCClientInterface socketClient = new RPCClient();
		for (Integer key : RoutingTable.getInstance().getRecords().keySet()) {
			try {
				TableRecord record = RoutingTable.getInstance().getRecords().get(key);
				socketClient.callAndGetResponse(record.getServer(), record.getPort(), leaveMessage);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

	/*
	 * length SER IP port file_name hops e.g 0047 SER 129.82.62.142 5070
	 * "Lord of the rings"
	 */
	@Override
	public void flooodTheMessage(String server, int port, String fileName, int TTL) {
		// check for TTL
		if (TTL > 1) {
			int updatedTTL = TTL - 1;
			
			String searchMessage = MessageGenerator.createSearchMessage(server, port, fileName, updatedTTL);
			
			//SocketClient socketClient = new UDPClient();
			RPCClientInterface socketClient=new RPCClient();
			for (Integer key : RoutingTable.getInstance().getRecords().keySet()) {
				TableRecord record = RoutingTable.getInstance().getRecords().get(key);
				
				if (!record.getServer().equals(server)) {
					try {
						socketClient.callAndGetResponse(record.getServer(), record.getPort(), searchMessage);
					} catch (SocketException e) {
						e.printStackTrace();
					} catch (UnknownHostException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	/*
	 * length SEROK no_files IP port hops filename1 filename2 e.g 0114 SEROK 3
	 * 129.82.128.1 2301 baby_go_home.mp3 baby_come_back.mp3 baby.mpeg
	 */
	@Override
	public void responseWithMatchingFiles(String server, int port, List<String> files) {
		if (files.size() > 0) {
			String searchOKMessage = MessageGenerator.createSearchOKMessage(Constants.NODE_IP, Constants.NODE_PORT, files);
			
			//SocketClient socketClient = new UDPClient();
			RPCClientInterface socketClient=new RPCClient();
			try {
				socketClient.callAndGetResponse(server, port, searchOKMessage);
			} catch (SocketException e) {
				e.printStackTrace();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("No matching file found in this node");
		}
	}

	@Override
	public void responseTheLeaving(String server, int port) {
//		for (Integer key : RoutingTable.getInstance().getRecords().keySet()) {
//			if (RoutingTable.getInstance().getRecords().get(key).getServer().equals(server) && RoutingTable.getInstance().getRecords().get(key).getPort() == port) {
//
//				RoutingTable.getInstance().getRecords()
//						.remove(key);
//			}
//
//		}

		String leaveOKMessage = MessageGenerator.createLeaveOKOverlayMessage();
		
		//SocketClient socketClient=new UDPClient();
		RPCClientInterface socketClient=new RPCClient();
		try {
			socketClient.callAndGetResponse(server, port, leaveOKMessage);
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void searchForMusicFile(String prefixOfMusic) {
		String messageSuffix = "";
		String fullMessage = "";

		messageSuffix += " SER "+ Constants.NODE_IP
				+ " " + Constants.NODE_PORT + " "+prefixOfMusic+" " + Constants.TTL;
		
		double d = (double) (messageSuffix.length() + 4) / (double) 10000;

		fullMessage += String.format("%.4f", d).substring(2);
		fullMessage += messageSuffix;
		System.out.println(fullMessage);
		//SocketClient socketClient=new UDPClient();
		RPCClientInterface socketClient=new RPCClient();
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
