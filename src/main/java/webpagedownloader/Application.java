package webpagedownloader;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import webpagedownloader.client.WebpageFetcher;
import webpagedownloader.parsing.ParsedWebsite;
import webpagedownloader.parsing.WebpageParser;

import java.io.IOException;
import java.util.LinkedList;

@SpringBootApplication
public class Application {

	private static ApplicationContext CONTEXT = new ClassPathXmlApplicationContext("config.xml");

	public static void main(String[] args) {
		String url = "http://www.tretton37.com";
		String rootFolder = "webpage";
		if(args.length > 0) {
			url = args[0];
		}
		if(args.length > 1) {
			rootFolder = args[1];
		}
		if(url != null && url.length() > 0 && url.endsWith("/")) {
			url = url.substring(0, url.length() - 1);
		}

		WebpageFetcher webpageFetcher = CONTEXT.getBean(WebpageFetcher.class);
		FileService fileService = CONTEXT.getBean("fileService", FileService.class);
		fileService.setRootFolder(rootFolder);

		LinkedList<String> links = new LinkedList<>();
		LinkedList<String> assets = new LinkedList<>();
		links.add("/main.html");
		try {
			while(!links.isEmpty()) {
				String link = links.poll();
				if(!fileService.exists(link)) {
					String data = webpageFetcher.fetchWebpage(url + link);
					fileService.storeFile(link , data);

					ParsedWebsite parsed = WebpageParser.parse(data);
					links.addAll(parsed.getHyperlinks());
					assets.addAll(parsed.getAssets());
				}
			}

			while(!assets.isEmpty()) {
				String asset = assets.poll();
				if(!fileService.exists(asset)) {
					String data = webpageFetcher.fetchWebpage(url + asset);
					fileService.storeFile(asset , data);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
