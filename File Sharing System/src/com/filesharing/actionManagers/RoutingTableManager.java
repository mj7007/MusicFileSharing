package com.filesharing.actionManagers;

public interface RoutingTableManager {
	
	void storeRoutingData(String server, int port, String userName);
	
	void removeRoutingData(String server, int port, String userName);
	
	void resetRoutingTable();

}
