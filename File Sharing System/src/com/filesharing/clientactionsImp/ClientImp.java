package com.filesharing.clientactionsImp;

import java.net.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.io.*;

import com.filesharing.actionManagersImp.FileManagerImp;
import com.filesharing.actionManagersImp.RegistrationManagerImp;
import com.filesharing.actionManagersImp.RoutingTableManagerImp;
import com.filesharing.actionManagersImp.WithinOverlayCommunicationManagerImp;
import com.filesharing.clientactions.Client;
import com.filesharing.dtos.TableRecord;
import com.filesharing.utils.RPCServerInterface;
import com.filesharing.utils.SocketServer;
import com.filesharing.utilsImp.Constants;
import com.filesharing.utilsImp.RPCServer;
import com.filesharing.utilsImp.UDPServer;

public class ClientImp implements Client {

	private RegistrationManagerImp registerManager;

	@Override
	public boolean registerAndJoinOverlay() {
		boolean isConnected = false;
		String serverIP = Constants.BOOTSTRAP_SERVER_IP;
		int serverPort = Constants.BOOTSTRAP_SERVER_PORT;
		
		String nodeIP = Constants.NODE_IP;
		int nodePort = Constants.NODE_PORT;
		String nodeUsername = Constants.NODE_USERNAME;
		
		registerManager = new RegistrationManagerImp();

		String outputString = registerManager.registerRequestAndGetResponse(serverIP, serverPort, nodeIP, nodePort, nodeUsername);
		String[] splited = outputString.split("\\s+");
		
		// check for valid reg message from the server
		if (splited.length < 3) {
			System.out.println("Unexpected message received from the server");
			System.exit(1);
		}
		
		// if valid
		if (splited[1].equals("REGOK")) {
			isConnected = true;
		}
		
		List<TableRecord> tableRecords = tokanizeMessageAndGetRecords(outputString);
		Iterator<TableRecord> it = tableRecords.iterator();
		
		System.out.println("Here are the table records");
		while (it.hasNext()) {
			TableRecord next = it.next();
			System.out.println(next.getServer() + " " + next.getPort() + " " + next.getUserName());
		}

		if (tableRecords.size() > 0) {
			List<TableRecord> randomRecords = chooseTwoRandomTableRecords(tableRecords);
			System.out.println("Randomly selected peers");
			Iterator<TableRecord> randIt = randomRecords.iterator();
			while (randIt.hasNext()) {
				TableRecord next = randIt.next();
				System.out.println(next.getServer() + " " + next.getPort() + " " + next.getUserName());
				
				// store randomly selected peers in rounting table
				new RoutingTableManagerImp().storeRoutingData(next.getServer(), next.getPort(), next.getUserName());
			}
		}
		
		// send the join message to peers
		new WithinOverlayCommunicationManagerImp().informTheJoining();
		
		return isConnected;
	}

	@Override
	public String serviceTheReceivedMessage(String message) {
		String[] splited = message.split("\\s+");
		
		// if search message received
		if (splited[1].equals("SER")) {
			// Format : length SER IP port file_name hops
			String server = splited[2];
			int port = Integer.parseInt(splited[3]);
			String fileName = splited[4];
			int TTL = Integer.parseInt(splited[5]);
			
			List<String> matchingFiles = new FileManagerImp().getMatchingFiles(fileName);
			new WithinOverlayCommunicationManagerImp().responseWithMatchingFiles(server, port, matchingFiles);
			new WithinOverlayCommunicationManagerImp().flooodTheMessage(server, port, fileName, TTL);
		}
		
		// if joing message received
		else if (splited[1].equals("JOIN")) {
			String server = splited[2];
			int port = Integer.parseInt(splited[3]);
			new RoutingTableManagerImp().storeRoutingData(server, port, null);
		} 
		
		// if leave message received
		else if (splited[1].equals("LEAVE")) {
			new WithinOverlayCommunicationManagerImp().responseTheLeaving(
					splited[2], Integer.parseInt(splited[3]));

		} else {
			
		}
		
		return message;
	}

	private List<TableRecord> tokanizeMessageAndGetRecords(String message) {
		List<TableRecord> recordList = new ArrayList<TableRecord>();

		String[] splited = message.split("\\s+");

		int numberOfPeers = 0;
		if (splited.length > 3) {
			numberOfPeers = Integer.parseInt(splited[2]);
		}
		if (numberOfPeers >= 1) {

			int messageTokenNumber = 3;

			for (int i = 0; i < numberOfPeers; i++) {
				TableRecord newRecord = new TableRecord();
				newRecord.setServer(splited[messageTokenNumber++]);
				newRecord.setPort(Integer
						.parseInt(splited[messageTokenNumber++]));
				newRecord.setUserName(splited[messageTokenNumber++]);
				recordList.add(newRecord);

			}
		}
		return recordList;

	}

	private List<TableRecord> chooseTwoRandomTableRecords(List<TableRecord> records) {
		List<TableRecord> inputRecords = records;
		List<TableRecord> outputRecords = new ArrayList<TableRecord>();
		int elementCount = records.size();

		int randomRecord = 0;
		Random rand = new Random();
		if (elementCount == 1) {
			outputRecords.add(inputRecords.get(0));
			return outputRecords;
		} else {
			randomRecord = rand.nextInt(elementCount);
			outputRecords.add(inputRecords.get(randomRecord));

			randomRecord = getRandomIndex(randomRecord, elementCount);
			outputRecords.add(inputRecords.get(randomRecord));

			return outputRecords;
		}

	}
	
	private int getRandomIndex(int exclusiveValue, int max) {
		Random rand = new Random();
		int randomNumber = rand.nextInt(max);
		if (randomNumber != exclusiveValue) {
			return randomNumber;
		} else {
			return getRandomIndex(exclusiveValue, max);
		}
	}

	@Override
	public void searchFile(String prefixOfFile) {
		new WithinOverlayCommunicationManagerImp()
				.searchForMusicFile(prefixOfFile);
	}

	@Override
	public void listenToNodes() {
		RPCServerInterface rpcServer=new RPCServer();
		rpcServer.startWebServer();
		
//		SocketServer socketServer = new UDPServer();
//		try {
//			socketServer.listenAndGetResponse(null, Constants.NODE_PORT, null);
//		} catch (SocketException e) {
//			e.printStackTrace();
//		} catch (UnknownHostException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}

}
