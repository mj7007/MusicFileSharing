package com.filesharing.globalitems;

import java.util.concurrent.ConcurrentHashMap;

import com.filesharing.dtos.TableRecord;

public class RoutingTable {

	private static RoutingTable routingTable;
	private ConcurrentHashMap<String, TableRecord> records;

	private RoutingTable() {
		records = new ConcurrentHashMap<String, TableRecord>();
	}

	public static RoutingTable getInstance() {
		if (routingTable == null) {
			routingTable = new RoutingTable();
		}
		return routingTable;
	}

	public ConcurrentHashMap<String, TableRecord> getRecords() {
		return records;
	}

	public void setRecords(ConcurrentHashMap<String, TableRecord> records) {
		this.records = records;
	}

}
