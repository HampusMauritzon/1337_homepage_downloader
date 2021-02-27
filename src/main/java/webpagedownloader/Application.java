package webpagedownloader;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import webpagedownloader.client.WebpageFetcher;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.time.Instant;
import java.util.Properties;

public class Application {
	private static final ApplicationContext CONTEXT = new ClassPathXmlApplicationContext("config.xml");
	private static final Properties PROPERTIES = loadProperties();

	public static void main(String[] args) throws URISyntaxException {
		Instant startTime = Instant.now();
		URI url = new URI(PROPERTIES.getProperty("website"));

		FileService fileService = CONTEXT.getBean(FileService.class);

		DownloaderExecutorService executorService = CONTEXT.getBean(DownloaderExecutorService.class);
		WebpageFetcher webpageFetcher = new WebpageFetcher(executorService, WebpageFetcher.createClient());
		AsyncResponseProcessor processor = new AsyncResponseProcessor(webpageFetcher, fileService);
		if (!fileService.exists(url)) {
			fileService.create(url);
			webpageFetcher.fetchWebpage(url, processor::hyperlinkResponseHandling);
		}

		executorService.waitUntilComplete();
		System.out.println("Download complete in " + Duration.between(startTime, Instant.now()));
		System.out.println("System shutting down...");
		executorService.shutdown();
		webpageFetcher.shutdown();
		System.out.println("Shutdown complete. Good Night!");
		System.exit(0);
	}

	public static Properties loadProperties() {
		String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
		String appConfigPath = rootPath + "application.properties";

		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream(appConfigPath));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return properties;
	}
}
