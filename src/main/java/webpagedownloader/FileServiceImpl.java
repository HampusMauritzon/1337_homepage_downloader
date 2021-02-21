package webpagedownloader;

import org.apache.logging.log4j.util.Strings;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileServiceImpl implements FileService {
	private String rootFolder;

	@Override
	public void setRootFolder(String rootFolder) {
		if(Strings.isNotEmpty(rootFolder) && !rootFolder.endsWith("/")) {
			rootFolder += "/";
		}
		this.rootFolder = rootFolder;
	}

	@Override
	public void storeFile(String filePath, String data) throws IOException {
		File file = getFile(filePath);

		if(!file.exists()) {
			Files.createDirectories(Paths.get(file.getParent()));
			file.createNewFile();
			FileWriter writer = new FileWriter(file);
			writer.write(data);
			writer.close();
		}
	}

	@Override
	public boolean exists(String filePath) {
		File file = getFile(filePath);
		return file.exists();
	}

	private File getFile(String filePath) {
		if(filePath.startsWith("/")) {
			filePath = filePath.substring(1);
		}
		if(filePath.endsWith("/") || filePath.isEmpty()) {
			filePath += "main.html";
		}
		//TODO getFileExtension is unstable, implement own file extension method.
		if(com.google.common.io.Files.getFileExtension(filePath).isEmpty()) {
			filePath += ".html";
		}
		return new File(rootFolder + filePath);
	}
}
