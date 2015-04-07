package com.musicsharing.actionManagersImp;

import com.musicsharing.actionManagers.RoutingTableManager;
import com.musicsharing.dtos.TableRecord;
import com.musicsharing.globalitems.RoutingTable;

public class RoutingTableManagerImp implements RoutingTableManager {
	
	@Override
	public void storeRoutingData(String server, int port, String userName) {
		TableRecord tr = new TableRecord(server, port, userName);
		RoutingTable.getInstance().getRecords().put(tr.hashCode(), tr);
	}

}
