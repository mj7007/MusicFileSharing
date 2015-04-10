package com.filesharing.utils;

import java.util.Iterator;
import java.util.List;

public class MessageGenerator {

	public static String createRegistrationMessage(String myServer, int myPort, String myUserName) {
		String messageSuffix = " REG " + myServer + " " + myPort + " " + myUserName;
		String fullMessage = generateFullMessage(messageSuffix);
		return fullMessage;
	}
	
	public static String createJoinOverlayMessage(String myNodeIP, int myNodePort) {
		String messageSuffix = " JOIN " + myNodeIP + " " + myNodePort;
		String fullMessage = generateFullMessage(messageSuffix);
		return fullMessage;
	}
	
	public static String createLeaveOverlayMessage(String nodeIP, int nodePort) {
		String messageSuffix = " LEAVE " + nodeIP + " " + nodePort;
		String fullMessage = generateFullMessage(messageSuffix);
		return fullMessage;
	}
	
	public static String createLeaveOKOverlayMessage() {
		String messageSuffix = " LEAVEOK 0";
		String fullMessage = generateFullMessage(messageSuffix);
		return fullMessage;
	}
	
	public static String createSearchMessage(String searchServer, int searchPort, String fileName, int TTL) {
		String messageSuffix = " SER " + searchServer + " " + searchPort + " " + fileName + " " + TTL;
		String fullMessage = generateFullMessage(messageSuffix);
		return fullMessage;
	}
	
	public static String createSearchOKMessage(String matchIP, int matchPort, List<String> files) {
		String messageSuffix = " SEROK " + files.size() + " " + matchIP + " " + matchPort + " " + 0;
		Iterator<String> it = files.iterator();
		while (it.hasNext()) {
			String fileName = it.next();
			messageSuffix += " " + fileName;
		}
		String fullMessage = generateFullMessage(messageSuffix);
		return fullMessage;
	}
	
	private static String generateFullMessage(String messageSuffix) {
		double length = (double) (messageSuffix.length() + 4) / (double) 10000;

		String fullMessage = String.format("%.4f", length).substring(2) + messageSuffix;
		return fullMessage;
	}

	public static String createUnregistrationMessage(String myNodeIP, int myNodePort, String myNodeUsername) {
		String messageSuffix = " UNREG " + myNodeIP + " " + myNodePort + " " + myNodeUsername;
		String fullMessage = generateFullMessage(messageSuffix);
		return fullMessage;
	
	}
	
}
