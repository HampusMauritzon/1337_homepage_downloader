package webpagedownloader;

import java.io.IOException;

public interface FileService {

	/**
	 * @param rootFolder the folder where the webpage should be stored.
	 */
	void setRootFolder(String rootFolder);

	/**
	 * Stores the data in the provided file path relative to the root folder.
	 */
	void storeFile(String filePath, String data) throws IOException;

	/**
	 * @param filePath the path to the file relative to the root folder.
	 * @return <code>true</code> if the file represented by the file path already exists, else retruns <code>false</code>
	 */
	boolean exists(String filePath);
}
