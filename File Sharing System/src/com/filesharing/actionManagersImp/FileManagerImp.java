package com.filesharing.actionManagersImp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.filesharing.actionManagers.FileManager;
import com.filesharing.globalitems.FileRepository;

public class FileManagerImp implements FileManager {

	/**
	 * Load file names to be distributed among nodes
	 */
	public static final String[] FILENAMES = { "Adventures of Tintin",
			"Jack and Jill", "Glee", "The Vampire Diarie", "King Arthur",
			"Windows XP", "Harry Potter", "Kung Fu Panda", "Lady Gaga",
			"Twilight", "Windows 8", "Mission Impossible", "Turn Up The Music",
			"Super Mario", "American Pickers", "Microsoft Office 2010",
			"Happy Feet", "Modern Family", "American Idol",
			"Hacking for Dummies" };

	@Override
	public void initiateFilesOfTheNode() {
		FileRepository.getInstance().setMusicFiles(selectRandomfiles());
		System.out.print("Set of files: ");
		Iterator<String> it = FileRepository.getInstance().getMusicFiles().iterator();
		while (it.hasNext()) {
			System.out.print(it.next() + ",");
		}
	}

	@Override
	public List<String> getMatchingFiles(String query) {
		List<String> matchingFiles = new ArrayList<String>();
		Iterator<String> it = FileRepository.getInstance().getMusicFiles().iterator();

		while (it.hasNext()) {
			String musicFileName = it.next();
			
			if (musicFileName.contains(query + "_") || musicFileName.contains("_" + query) || 
					musicFileName.contains("_" + query + "_") || musicFileName.contains(query)) {
				matchingFiles.add(musicFileName);
			}
		}
		return matchingFiles;
	}

	/**
	 * Select 3-5 random file names
	 * 
	 * @return list of random file names between 3 - 5
	 */
	public static final List<String> selectRandomfiles() {
		List<String> fileList = new ArrayList<String>(Arrays.asList(FILENAMES));
		List<String> randomList = new ArrayList<>();

		Collections.shuffle(fileList);
		int numberOfFilesInNode = randInt(3, 5);

		for (int j = 0; j < numberOfFilesInNode; j++) {
			String updatedFileName = fileList.get(j).replaceAll(" ", "_");
			randomList.add(updatedFileName);
		}

		return randomList;
	}

	/**
	 * select random sample of indexes in range
	 * 
	 * @param numberOfFilesInNode
	 *            Number of files to be selected
	 * @return random indexes of file array
	 */
	private static Set<Integer> getRandomNumberIndexes(int numberOfFilesInNode) {
		Set<Integer> rnindexes = new HashSet<Integer>();
		while (rnindexes.size() != numberOfFilesInNode) {
			rnindexes.add(randInt(0, FILENAMES.length));
		}
		return rnindexes;
	}

	/**
	 * Generate random integer in the given range
	 * 
	 * @param min
	 *            minimum value for random number
	 * @param max
	 *            maximum value for random number
	 * @return random integer between min and max
	 */
	public static int randInt(int min, int max) {

		// NOTE: Usually this should be a field rather than a method
		// variable so that it is not re-seeded every call.
		Random rand = new Random();

		// nextInt is normally exclusive of the top value,
		// so add 1 to make it inclusive
		int randomNum = rand.nextInt((max - min) + 1) + min;

		return randomNum;
	}

}
