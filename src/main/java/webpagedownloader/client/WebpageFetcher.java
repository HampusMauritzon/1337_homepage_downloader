package webpagedownloader.client;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;

@Component
public class WebpageFetcher {

	@Autowired private WebpageResponseHandler responseHandler;
	private CloseableHttpClient httpClient = HttpClients.createDefault();

	public void setResponseHandler(WebpageResponseHandler responseHandler) {
		this.responseHandler = responseHandler;
	}

	public String fetchWebpage(String urlName) throws IOException {
		return httpClient.execute(new HttpGet(urlName), responseHandler);
	}

}
