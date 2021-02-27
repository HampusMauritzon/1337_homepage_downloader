package webpagedownloader;

import java.net.URI;

public interface FileService {

	void create(URI filePath);

	/**
	 * Stores the data in the provided file path relative to the root folder.
	 */
	void storeFile(URI filePath, String data);

	/**
	 * @param filePath the path to the file relative to the root folder.
	 * @return <code>true</code> if the file represented by the file path already exists, else retruns <code>false</code>
	 */
	boolean exists(URI filePath);

	boolean notExists(URI filePath);
}
