package com.filesharing.actionManagers;

import java.util.List;

public interface FileManager {
	
	public void initiateFilesOfTheNode();
	
	public List<String> getMatchingFiles(String prefix);
	
}
