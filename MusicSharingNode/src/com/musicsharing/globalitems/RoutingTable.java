package com.musicsharing.globalitems;

import java.util.Hashtable;

import com.musicsharing.dtos.TableRecord;

public class RoutingTable {

	private static RoutingTable routingTable;

	private Hashtable<Integer, TableRecord> records;

	private RoutingTable() {
		records = new Hashtable<Integer, TableRecord>();
	}

	public static RoutingTable getInstance() {
		if (routingTable == null) {
			routingTable = new RoutingTable();
		}
		return routingTable;
	}

	public Hashtable<Integer, TableRecord> getRecords() {
		return records;
	}

	public void setRecords(Hashtable<Integer, TableRecord> records) {
		this.records = records;
	}

}
