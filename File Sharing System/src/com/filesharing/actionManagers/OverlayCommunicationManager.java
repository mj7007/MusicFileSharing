package com.filesharing.actionManagers;

import java.util.List;

public interface OverlayCommunicationManager {
	
	public void searchForFile(String query);
	
	public void informTheJoining();
	
	public void informTheLeaving();
	
	public void responseTheLeaving(String server,int port);
	
	public void flooodTheMessage(String server,int port,String fileName,int TTL);
	
	public void responseWithMatchingFiles(String server,int port,List<String> files);

}
