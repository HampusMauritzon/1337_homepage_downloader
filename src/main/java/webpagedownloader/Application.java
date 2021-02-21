package webpagedownloader;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import webpagedownloader.client.WebpageFetcher;

import java.io.IOException;

@SpringBootApplication
public class Application {

	private static ApplicationContext CONTEXT = new ClassPathXmlApplicationContext("config.xml");

	public static void main(String[] args) {
		String url = "http://www.tretton37.com/";
		String rootFolder = "webpage";

		WebpageFetcher webpageFetcher = CONTEXT.getBean(WebpageFetcher.class);
		FileManager fileManager = CONTEXT.getBean(FileManager.class);
		fileManager.setRootFolder(rootFolder);
		try {
			String body = webpageFetcher.fetchWebpage(url);
			fileManager.storeFile("main.html" , body);

			WebpageParser.parse(body);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
