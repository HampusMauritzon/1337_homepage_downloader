package webpagedownloader;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import webpagedownloader.client.WebpageFetcher;
import webpagedownloader.parsing.ParsedWebsite;
import webpagedownloader.parsing.WebpageParser;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.time.Instant;
import java.util.LinkedList;

@SpringBootApplication
public class Application {

	private static ApplicationContext CONTEXT = new ClassPathXmlApplicationContext("config.xml");

	public static void main(String[] args) {
		Instant startTime = Instant.now();
		URI url = null;
		try {
			url = new URI("http://tretton37.com/");
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

		WebpageFetcher webpageFetcher = CONTEXT.getBean(WebpageFetcher.class);
		FileService fileService = CONTEXT.getBean("fileService", FileService.class);

		LinkedList<URI> links = new LinkedList<>();
		LinkedList<URI> assets = new LinkedList<>();
		links.add(url);

		try {
			while(!links.isEmpty()) {
				URI link = links.poll();
				if(!fileService.exists(link)) {
					System.out.println("Fetching " + link.toString());
					String data = webpageFetcher.fetchWebpage(link);
					fileService.storeFile(link , data);

					ParsedWebsite parsed = WebpageParser.parse(link, data);
					links.addAll(parsed.getHyperlinks());
					assets.addAll(parsed.getAssets());
				}
			}

			while(!assets.isEmpty()) {
				URI asset = assets.poll();
				if(!fileService.exists(asset)) {
					System.out.println("Fetching " + asset.toString());
					String data = webpageFetcher.fetchWebpage(asset);
					fileService.storeFile(asset , data);
				}
			}
			System.out.println("Download complete in " + Duration.between(startTime, Instant.now()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("System shutting down.");
	}
}
