package webpagedownloader;

import org.apache.logging.log4j.util.Strings;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileManager {
	private String rootFolder;

	public void setRootFolder(String rootFolder) {
		if(Strings.isNotEmpty(rootFolder) && !rootFolder.endsWith("/")) {
			rootFolder += "/";
		}
		this.rootFolder = rootFolder;
	}

	public void storeFile(String filePath, String body) throws IOException {
		File file = new File(rootFolder + filePath);

		if(!file.exists()) {
			Files.createDirectories(Paths.get(file.getParent()));
			file.createNewFile();
			FileWriter writer = new FileWriter(file);
			writer.write(body);
			writer.close();
		}
	}
}
