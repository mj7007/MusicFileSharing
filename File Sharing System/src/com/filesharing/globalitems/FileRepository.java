package com.filesharing.globalitems;

import java.util.ArrayList;
import java.util.List;

public class FileRepository {

	static FileRepository fileRepo;
	List<String> musicFiles;

	private FileRepository() {
		 musicFiles=new ArrayList<String>();
	}

	public static FileRepository getInstance() {
		if (fileRepo == null) {
			fileRepo = new FileRepository();

		}
		return fileRepo;

	}

	public List<String> getMusicFiles() {
		return musicFiles;
	}

	public void setMusicFiles(List<String> musicFiles) {
		this.musicFiles = musicFiles;
	}

}
