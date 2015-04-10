package com.filesharing.actionManagersImp;

import com.filesharing.actionManagers.RoutingTableManager;
import com.filesharing.dtos.TableRecord;
import com.filesharing.globalitems.RoutingTable;

public class RoutingTableManagerImp implements RoutingTableManager {
	
	@Override
	public void storeRoutingData(String server, int port, String userName) {
		TableRecord tr = new TableRecord(server, port, userName);
		RoutingTable.getInstance().getRecords().put(tr.hashCode(), tr);
		System.out.println("Routing Table Size : " + RoutingTable.getInstance().getRecords().size());
	}

	@Override
	public void removeRoutingData(String server, int port, String userName) {
		TableRecord tr = new TableRecord(server, port, userName);
		RoutingTable.getInstance().getRecords().remove(tr.hashCode());
		System.out.println("Routing Table Size : " + RoutingTable.getInstance().getRecords().size());
	}

}
