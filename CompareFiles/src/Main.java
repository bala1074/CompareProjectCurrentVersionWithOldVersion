import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class Main {
	static String v1URL = "/Users/bala_in/Documents/Freelance/1CompateFiles/ejubi/";
	static String v2URL = "/Users/bala_in/Documents/Freelance/1CompateFiles/PHP Pro Bid v7.7";
	static String v3DiffURL="/Users/bala_in/Documents/Freelance/1CompateFiles/Diff";
	public static void main(String[] args) throws FileNotFoundException, IOException {
		final File v1 = new File(v1URL);
		final File v2 = new File(v2URL);
		HashSet<String> v1Files = listFilesForFolder(v1, "");
		HashSet<String> v2Files = listFilesForFolder(v2, "");

		/*
		v1Files = new HashSet<>();
		v2Files = new HashSet<>();
		v1Files.add("/mod-rewrite.php");
		v2Files.add("/mod-rewrite.php");
		*/
		
		int diffFiles = 0, newFiles = 0, filesCount = 1;

		Iterator<String> files = v2Files.iterator();
		while (files.hasNext()) {
			filesCount++;
			String currentFile = files.next();
			if (v1Files.contains(currentFile)) {// compare and show diff
				// read file of v1
				HashSet<String> v1File = readFileV1File(v1URL + currentFile);
				// read file of v2
				ArrayList<String> v2File = readFileV2File(v2URL + currentFile);
				
				//System.out.println(v1File);
				//System.out.println(v2File);

				boolean compare = false;
				ArrayList<String> fileDiff = new ArrayList<>();
				// compare files
				for (int i = 0; i < v2File.size(); i++) {
					if (!v1File.contains(v2File.get(i)) && !v2File.get(i).startsWith("*")) {
						compare = true;
						fileDiff.add(v2File.get(i));
						// System.out.println(v2File.get(i));
					}
				}
				if (compare) {
					diffFiles++;
					System.out.println("File " + (diffFiles) + " " + currentFile);
					createFile(currentFile, fileDiff);
				}

			} else {// show hole file
				newFiles++;
				ArrayList<String> v2File = readFileV2File(v2URL + "/" + currentFile);// latest file, not present in v1
				createFile(currentFile, v2File);

			}
		}
		System.out.println("Total Files: "+ filesCount +", Diff Files: "+diffFiles + ", New Files " + newFiles);
	}

	public static HashSet<String> listFilesForFolder(final File folder, String path) {
		HashSet<String> files = new HashSet<>();
		for (final File fileEntry : folder.listFiles()) {
			String currentFile = fileEntry.getName();
			// System.out.println(currentFile);
			if (fileEntry.isDirectory()) {
				// System.out.println(currentFile + " --");
				// if file is a directory
				files.addAll(listFilesForFolder(fileEntry, path + "/" + currentFile)); // calling recursion to get files
																						// inside.
			} else {
				if (!(currentFile.startsWith(".") || currentFile.endsWith(".zip") || currentFile.endsWith(".png")
						|| currentFile.endsWith(".jpg") || currentFile.endsWith(".ico")
						|| currentFile.endsWith(".gz"))) { // avoiding
															// the
															// hidden
															// files

					files.add(path + "/" + currentFile);
					// System.out.println(fileEntry.getName());
				}
			}
		}
		return files;
	}

	public static HashSet<String> readFileV1File(String file) throws FileNotFoundException, IOException {
		HashSet<String> reading = new HashSet<>();
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line;
			while ((line = br.readLine()) != null) {
				// process the line.
				reading.add(line.trim());
			}
		}
		return reading;
	}

	public static ArrayList<String> readFileV2File(String file) throws FileNotFoundException, IOException {
		ArrayList<String> reading = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line;
			while ((line = br.readLine()) != null) {
				// process the line.
				reading.add(line.trim());
			}
		}
		return reading;
	}

	public static void createFile(String fileName, ArrayList<String> fileData) throws IOException {
		//System.out.println(fileData);
		Path path = Paths.get(v3DiffURL+fileName);
        Files.createDirectories(path.getParent());
		Files.write(path, fileData, Charset.forName("UTF-8"));
	}
}
