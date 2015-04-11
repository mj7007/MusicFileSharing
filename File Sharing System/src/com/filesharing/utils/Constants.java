package com.filesharing.utils;

public class Constants {
	
	public enum RUN_MODE {
		UDP, RPC
	}
	
	public static String BOOTSTRAP_SERVER_IP = "172.20.10.2";
	public static int BOOTSTRAP_SERVER_PORT = 2000;
	
	public static String NODE_IP = "172.20.10.2";
	public static int NODE_PORT = 7000;
	public static String NODE_USERNAME = "abcd12mj";
	
	public static int NODE_UDP_PORT = 8000;
	
	public static RUN_MODE MODE = RUN_MODE.UDP;
	
	public static final int TTL = 5;
}