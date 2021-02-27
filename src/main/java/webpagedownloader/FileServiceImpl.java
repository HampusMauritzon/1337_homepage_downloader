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
	public void create(URI filePath) {
		File file = getFile(filePath);
		System.out.println("Creating: " + file.getAbsolutePath());
		if (Strings.isNotEmpty(file.getParent())) {
			try {
				Files.createDirectories(Paths.get(file.getParent()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void storeFile(URI filePath, String data) {
		if (data == null) {
			return;
		}
		File file = getFile(filePath);
		try {
			FileWriter writer = new FileWriter(file);
			writer.write(data);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean exists(URI filePath) {
		return getFile(filePath).exists();
	}

	@Override
	public boolean notExists(URI filePath) {
		return !getFile(filePath).exists();
	}

	private File getFile(URI filePath) {
		String path = filePath.getHost() + filePath.getPath();
		if (path.endsWith("/") || path.isEmpty()) {
			path += "main.html";
		}
		if (com.google.common.io.Files.getFileExtension(path).isEmpty()) {
			path += ".html";
		}
		return new File(path);
	}
}
