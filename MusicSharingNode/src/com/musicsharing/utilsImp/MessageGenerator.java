package com.musicsharing.utilsImp;

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
	
	private static String generateFullMessage(String messageSuffix) {
		double length = (double) (messageSuffix.length() + 4) / (double) 10000;

		String fullMessage = String.format("%.4f", length).substring(2) + messageSuffix;
		return fullMessage;
	}
	
}
