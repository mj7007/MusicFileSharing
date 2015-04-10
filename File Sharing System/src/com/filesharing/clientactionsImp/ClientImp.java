package com.filesharing.clientactionsImp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.filesharing.actionManagers.OverlayCommunicationManager;
import com.filesharing.actionManagers.RoutingTableManager;
import com.filesharing.actionManagersImp.FileManagerImp;
import com.filesharing.actionManagersImp.OverlayCommunicationManagerImp;
import com.filesharing.actionManagersImp.RegistrationManagerImp;
import com.filesharing.actionManagersImp.RoutingTableManagerImp;
import com.filesharing.clientactions.Client;
import com.filesharing.dtos.TableRecord;
import com.filesharing.globalitems.RoutingTable;
import com.filesharing.utils.Constants;
import com.filesharing.utils.RPCServer;
import com.filesharing.utilsImp.RPCServerImp;

public class ClientImp implements Client {

	private RegistrationManagerImp registerManager;
	private OverlayCommunicationManager overlayCommunicationManager;
	private RoutingTableManager routingTableManager;
	
	private RPCServer rpcServer;
	
	private List<TableRecord> allPeers;

	public ClientImp() {
		overlayCommunicationManager = new OverlayCommunicationManagerImp();
		registerManager = new RegistrationManagerImp();
		routingTableManager = new RoutingTableManagerImp();
	}

	@Override
	public boolean registerWithBSServer() {
		String outputString = registerManager.registerRequestAndGetResponse(Constants.BOOTSTRAP_SERVER_IP, 
				Constants.BOOTSTRAP_SERVER_PORT, Constants.NODE_IP, Constants.NODE_PORT, Constants.NODE_USERNAME);
		
		String[] splited = outputString.split("\\s+");

		// check for valid reg message from the server
		if (splited.length < 3) {
			System.out.println("Unexpected message received from the server");
			System.exit(1);
		}

		// if valid
		if (splited[1].equals("REGOK")) {
			System.out.println("Registered with BS Server");
			// extract all peers
			allPeers = tokanizeMessageAndGetRecords(outputString);
			return true;
		}

		return false;
	}
	
	@Override
	public boolean unregisterFromBServer() {
		String outputString = registerManager.unregisterRequestAndGetResponse(Constants.BOOTSTRAP_SERVER_IP, Constants.BOOTSTRAP_SERVER_PORT,
				Constants.NODE_IP, Constants.NODE_PORT, Constants.NODE_USERNAME);
		String[] splited = outputString.split("\\s+");

		// if valid
		if (splited[1].equals("UNREGOK")) {
			System.out.println("Unregistered from BS Server");
			return true;
		}
		
		return false;
	}
	
	@Override
	public void joinTheOverlay() {
		if (allPeers.size() > 0) {
			// existing peers
			System.out.println("Here are the table records");
			Iterator<TableRecord> it = allPeers.iterator();
			while (it.hasNext()) {
				TableRecord next = it.next();
				System.out.println(next.getServer() + " " + next.getPort() + " " + next.getUserName());
			}
			
			// randomly select one or two peers
			List<TableRecord> randomRecords = chooseTwoRandomTableRecords(allPeers);
			System.out.println("Randomly selected peers");
			Iterator<TableRecord> randIt = randomRecords.iterator();
			while (randIt.hasNext()) {
				TableRecord next = randIt.next();
				System.out.println(next.getServer() + " " + next.getPort() + " " + next.getUserName());

				// store randomly selected peers in rounting table
				routingTableManager.storeRoutingData(next.getServer(), next.getPort(), next.getUserName());
			}
		} else {
			System.out.println("No peers yet");
		}
	
		// send the join message to peers
		overlayCommunicationManager.informTheJoining();
	}
	
	@Override
	public void leaveTheOverlay() {
		if (RoutingTable.getInstance().getRecords().size() > 0) {
			overlayCommunicationManager.informTheLeaving();
		} else {
			// there's no peers to inform leaving
			unregisterFromBServer();
		}
	}

	@Override
	public String serviceTheReceivedMessage(String message) {
		String[] splited = message.split("\\s+");
		String command = splited[1];
		
		// if search message received
		if (command.equals("SER")) {
			// Format : length SER IP port file_name hops
			String server = splited[2];
			int port = Integer.parseInt(splited[3]);
			String fileName = splited[4];
			int TTL = Integer.parseInt(splited[5]);

			List<String> matchingFiles = new FileManagerImp().getMatchingFiles(fileName);
			overlayCommunicationManager.responseWithMatchingFiles(server, port, matchingFiles);
			overlayCommunicationManager.flooodTheMessage(server, port, fileName, TTL);
		}

		// if joing message received
		else if (command.equals("JOIN")) {
			String server = splited[2];
			int port = Integer.parseInt(splited[3]);
			new RoutingTableManagerImp().storeRoutingData(server, port, null);
		}

		// if leave message received
		else if (command.equals("LEAVE")) {
			String server = splited[2];
			int port = Integer.parseInt(splited[3]);
			new RoutingTableManagerImp().removeRoutingData(server, port, null);

			overlayCommunicationManager.responseTheLeaving(server, port);
		}

		// if leave message ok received
		else if (command.equals("LEAVEOK")) {
			unregisterFromBServer();
		}

		else {

		}

		return message;
	}

	@Override
	public void searchFile(String prefixOfFile) {
		overlayCommunicationManager.searchForMusicFile(prefixOfFile);
	}

	@Override
	public void listenToNodes() {
		if (rpcServer == null) {
			rpcServer = new RPCServerImp();
		}
		rpcServer.startWebServer();

		// SocketServer socketServer = new UDPServer();
		// try {
		// socketServer.listenAndGetResponse(null, Constants.NODE_PORT, null);
		// } catch (SocketException e) {
		// e.printStackTrace();
		// } catch (UnknownHostException e) {
		// e.printStackTrace();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
	}
	
	@Override
	public void stopListeningToNodes() {
		if (rpcServer != null) {
			rpcServer.stopWebServer();
		}
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
				newRecord.setPort(Integer.parseInt(splited[messageTokenNumber++]));
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

}
