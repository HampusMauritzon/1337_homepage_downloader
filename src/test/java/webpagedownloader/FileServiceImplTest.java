package webpagedownloader;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileServiceImplTest {

	private FileServiceImpl fileServiceImpl;

	@BeforeEach
	public void setup() {
		fileServiceImpl = new FileServiceImpl();
	}

	@Test
	public void testCreateFile() throws URISyntaxException {
		fileServiceImpl.create(new URI("http://testRoot/webpage.html"));

		File file = new File("testRoot/webpage.html");
		assertTrue(file.exists());
		assertTrue(file.isFile());

		//Cleanup
		if (file.exists()) {
			file.delete();
		}
		file = new File("testRoot");
		if (file.exists()) {
			file.deleteOnExit();
		}
	}

	@Test
	public void testStoreFile() throws IOException, URISyntaxException {
		fileServiceImpl.create(new URI("http://testRoot/webpage.html"));
		File file = new File("testRoot/webpage.html");

		fileServiceImpl.storeFile(new URI("http://testRoot/webpage.html"), "<html>Some test data.</html>");
		FileReader fileReader = new FileReader(file);
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		String text = bufferedReader.lines().collect(Collectors.joining());
		assertEquals(text, "<html>Some test data.</html>");

		//Cleanup
		bufferedReader.close();
		fileReader.close();
		if (file.exists()) {
			file.delete();
		}
		file = new File("testRoot");
		if (file.exists()) {
			file.deleteOnExit();
		}
	}

	@Test
	public void testExists() throws URISyntaxException {
		URI url = new URI("http://testRoot/webpage_e.html");
		assertFalse(fileServiceImpl.exists(url));

		fileServiceImpl.create(url);

		assertTrue(fileServiceImpl.exists(url));

		//cleanup
		File file = new File("testRoot/webpage_e.html");
		if (file.exists()) {
			file.delete();
		}
		file = new File("testRoot");
		if (file.exists()) {
			file.deleteOnExit();
		}
	}
}
