package webpagedownloader.client;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;

@Component
public class WebpageFetcher {

	@Autowired private WebpageResponseHandler responseHandler;
	private CloseableHttpClient httpClient = HttpClients.createDefault();

	public void setResponseHandler(WebpageResponseHandler responseHandler) {
		this.responseHandler = responseHandler;
	}

	public String fetchWebpage(URI url) throws IOException {
		try {
			return httpClient.execute(new HttpGet(url), responseHandler);
		} catch(ClientProtocolException | HttpHostConnectException e) {
			System.err.print(e.getMessage());
			System.err.println(". URI: " + url.toString());
		}
		return null;
	}
}
