package webpagedownloader;

import org.apache.logging.log4j.util.Strings;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileServiceImpl implements FileService {

	@Override
	public void storeFile(URI filePath, String data) throws IOException {
		if(data == null) {
			return;
		}
		File file = getFile(filePath);
		if(!file.exists()) {
			System.out.println("Storing: " + file.getAbsolutePath());
			if (Strings.isNotEmpty(file.getParent())) {
				Files.createDirectories(Paths.get(file.getParent()));
			}
			file.createNewFile();
			FileWriter writer = new FileWriter(file);
			writer.write(data);
			writer.close();
		}
	}

	@Override
	public boolean exists(URI filePath) {
		return getFile(filePath).exists();
	}

	private File getFile(URI filePath) {
		String path = filePath.getHost() + filePath.getPath();
		if(path.endsWith("/") || path.isEmpty()) {
			path += "main.html";
		}
		//TODO getFileExtension is unstable, implement own file extension method.
		if(com.google.common.io.Files.getFileExtension(path).isEmpty()) {
			path += ".html";
		}
		return new File(path);
	}
}
