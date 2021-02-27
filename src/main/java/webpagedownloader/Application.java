package webpagedownloader;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import webpagedownloader.client.HttpRequestHandler;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.time.Instant;

public class Application {
	private static final ApplicationContext CONTEXT = new ClassPathXmlApplicationContext("config.xml");

	public static void main(String[] args) throws URISyntaxException {
		Instant startTime = Instant.now();
		URI url = new URI("https://tretton37.com/");

		FileService fileService = CONTEXT.getBean(FileService.class);

		DownloaderExecutorService executorService = CONTEXT.getBean(DownloaderExecutorService.class);
		HttpRequestHandler httpRequestHandler = new HttpRequestHandler(executorService, HttpRequestHandler.createClient());
		AsyncResponseProcessor processor = new AsyncResponseProcessor(httpRequestHandler, fileService);
		if (!fileService.exists(url)) {
			fileService.create(url);
			httpRequestHandler.fetchWebpage(url, processor::hyperlinkResponseHandling);
		}

		executorService.waitUntilComplete();
		System.out.println("Download complete in " + Duration.between(startTime, Instant.now()));
		System.out.println("System shutting down...");
		executorService.shutdown();
		httpRequestHandler.shutdown();
		System.out.println("Shutdown complete. Good Night!");
		System.exit(0);
	}
}
