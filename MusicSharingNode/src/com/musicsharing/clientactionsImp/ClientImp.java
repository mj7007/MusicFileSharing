package com.musicsharing.clientactionsImp;

import java.net.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.io.*;

import com.musicsharing.actionManagersImp.FileManagerImp;
import com.musicsharing.actionManagersImp.RegistrationManagerImp;
import com.musicsharing.actionManagersImp.RoutingTableManagerImp;
import com.musicsharing.actionManagersImp.WithinOverlayCommunicationManagerImp;
import com.musicsharing.clientactions.Client;
import com.musicsharing.dtos.TableRecord;
import com.musicsharing.utils.SocketServer;
import com.musicsharing.utilsImp.Constants;
import com.musicsharing.utilsImp.UDPServer;

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
			System.out.print(next.getServer() + " ");
			System.out.print(next.getPort() + " ");
			System.out.print(next.getUserName() + " ");
			System.out.println();
		}

		if (tableRecords.size() > 0) {
			List<TableRecord> randomRecords = chooseTwoRandomTableRecords(tableRecords);
			System.out.println("Randomly selected peers");
			Iterator<TableRecord> it2 = randomRecords.iterator();
			while (it2.hasNext()) {
				TableRecord next = it2.next();
				System.out.print(next.getServer() + " ");
				System.out.print(next.getPort() + " ");
				System.out.print(next.getUserName() + " ");
				System.out.println();
				new RoutingTableManagerImp().storeRoutingData(next.getServer(), next.getPort(), next.getUserName());
				new WithinOverlayCommunicationManagerImp().informTheJoining();
			}
		}
		return isConnected;

	}

	@Override
	public String serviceTheReceivedMessage(String message) {
		String[] splited = message.split("\\s+");
		if (splited[1].equals("SER")) {
			// length SER IP port file_name hops
			List<String> matchingFiles = new FileManagerImp()
					.getMatchingFiles(splited[4]);
			new WithinOverlayCommunicationManagerImp()
					.responseWithMatchingFiles(splited[2],
							Integer.parseInt(splited[3]), matchingFiles);
			new WithinOverlayCommunicationManagerImp().flooodTheMessage(
					splited[2], Integer.parseInt(splited[3]), splited[4],
					Integer.parseInt(splited[5]));

		} else if (splited[1].equals("JOIN")) {
			new RoutingTableManagerImp().storeRoutingData(splited[2],
					Integer.parseInt(splited[3]), "");

		} else if (splited[1].equals("LEAVE")) {
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

	private List<TableRecord> chooseTwoRandomTableRecords(
			List<TableRecord> records) {
		List<TableRecord> inputRecords = records;
		List<TableRecord> outputRecords = new ArrayList<TableRecord>();
		int elementCount = records.size();

		int randomRecord = 0;
		Random rand = new Random();
		if (elementCount == 1) {

			outputRecords.add(inputRecords.get(0));

			return outputRecords;
		} else {
			randomRecord = (int) Math.abs(rand.nextInt(elementCount - 1));
			outputRecords.add(inputRecords.get(randomRecord));

			randomRecord = (int) Math.abs(rand.nextInt(elementCount - 1));
			outputRecords.add(inputRecords.get(randomRecord));

			return outputRecords;
		}

	}

	@Override
	public void searchFile(String prefixOfFile) {
		new WithinOverlayCommunicationManagerImp()
				.searchForMusicFile(prefixOfFile);
	}

	@Override
	public void listenToNodes() {
		SocketServer socketServer = new UDPServer();
		try {
			socketServer.listenAndGetResponse(null, Constants.NODE_PORT, null);
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
