package com.filesharing.actionManagersImp;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.List;

import com.filesharing.actionManagers.OverlayCommunicationManager;
import com.filesharing.dtos.TableRecord;
import com.filesharing.globalitems.RoutingTable;
import com.filesharing.utils.Constants;
import com.filesharing.utils.MessageGenerator;
import com.filesharing.utils.RPCClient;
import com.filesharing.utils.SocketClient;
import com.filesharing.utils.Constants.RUN_MODE;
import com.filesharing.utilsImp.RPCClientImp;
import com.filesharing.utilsImp.UDPClient;

public class OverlayCommunicationManagerImp implements OverlayCommunicationManager {

	private SocketClient socketClient;
	private RPCClient rpcClient;
	
	public OverlayCommunicationManagerImp() {
		if (Constants.MODE == RUN_MODE.UDP) {
			socketClient = new UDPClient();
		}
		
		else if (Constants.MODE == RUN_MODE.RPC) {
			rpcClient = new RPCClientImp();
		}
		
	}
	
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
		
		Iterator<TableRecord> it = RoutingTable.getInstance().getRecords().values().iterator();
		while (it.hasNext()) {
			TableRecord record = it.next();
			
			try {
				
				if (Constants.MODE == RUN_MODE.UDP) {
					socketClient.callAndGetResponse(record.getServer(), record.getPort(), joinMessage);
				}
				
				else if (Constants.MODE == RUN_MODE.RPC) {
					rpcClient.callAndGetResponse(record.getServer(), record.getPort(), joinMessage);
				}
				
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
		String leaveMessage = MessageGenerator.createLeaveOverlayMessage(Constants.NODE_IP, Constants.NODE_PORT);
		
		Iterator<TableRecord> it = RoutingTable.getInstance().getRecords().values().iterator();
		while (it.hasNext()) {
			TableRecord record = it.next();
			
			try {
				if (Constants.MODE == RUN_MODE.UDP) {
					socketClient.callAndGetResponse(record.getServer(), record.getPort(), leaveMessage);
				}
				
				else if (Constants.MODE == RUN_MODE.RPC) {
					rpcClient.callAndGetResponse(record.getServer(), record.getPort(), leaveMessage);
				}
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
			
			Iterator<TableRecord> it = RoutingTable.getInstance().getRecords().values().iterator();
			while (it.hasNext()) {
				TableRecord record = it.next();

				if (!record.getServer().equals(server)) {
					try {
						if (Constants.MODE == RUN_MODE.UDP) {
							socketClient.callAndGetResponse(record.getServer(), record.getPort(), searchMessage);
						}
						
						else if (Constants.MODE == RUN_MODE.RPC) {
							rpcClient.callAndGetResponse(record.getServer(), record.getPort(), searchMessage);
						}
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
			
			try {
				if (Constants.MODE == RUN_MODE.UDP) {
					socketClient.callAndGetResponse(server, port, searchOKMessage);
				}
				
				else if (Constants.MODE == RUN_MODE.RPC) {
					rpcClient.callAndGetResponse(server, port, searchOKMessage);
				}
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
		String leaveOKMessage = MessageGenerator.createLeaveOKOverlayMessage();
	
		try {
			if (Constants.MODE == RUN_MODE.UDP) {
				socketClient.callAndGetResponse(server, port, leaveOKMessage);
			}
			
			else if (Constants.MODE == RUN_MODE.RPC) {
				rpcClient.callAndGetResponse(server, port, leaveOKMessage);
			}
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void searchForFile(String query) {
		flooodTheMessage(Constants.NODE_IP, Constants.NODE_PORT, query, Constants.TTL);
	}

}
