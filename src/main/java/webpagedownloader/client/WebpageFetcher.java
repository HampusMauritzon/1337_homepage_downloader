package webpagedownloader.client;

import org.apache.hc.client5.http.async.methods.SimpleHttpRequests;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.async.HttpAsyncClients;
import org.apache.hc.client5.http.impl.nio.PoolingAsyncClientConnectionManager;
import org.springframework.stereotype.Component;
import webpagedownloader.DownloaderExecutorService;

import java.io.IOException;
import java.net.URI;
import java.util.function.BiConsumer;


@Component
public class WebpageFetcher {

	private static final int MAX_CONNECTIONS_TOTAL = 10;
	private static final int MAX_CONNECTIONS_PER_ROUTE = 5;

	private final CloseableHttpAsyncClient client;
	private final DownloaderExecutorService executorService;


	public WebpageFetcher(DownloaderExecutorService executorService, CloseableHttpAsyncClient client) {
		this.executorService = executorService;
		this.client = client;
		client.start();
	}

	public void fetchWebpage(URI url, BiConsumer<URI, String> responseHandler) {
		System.out.println("Fetching " + url.toString());
		client.execute(SimpleHttpRequests.get(url), new WebpageFutureCallback(executorService, url, responseHandler));
	}

	public void shutdown() {
		try {
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static CloseableHttpAsyncClient createClient() {
		PoolingAsyncClientConnectionManager connectionManager = new PoolingAsyncClientConnectionManager();
		connectionManager.setDefaultMaxPerRoute(MAX_CONNECTIONS_PER_ROUTE);
		connectionManager.setMaxTotal(MAX_CONNECTIONS_TOTAL);

		return HttpAsyncClients.custom()
				.setConnectionManager(connectionManager)
				.build();
	}
}
