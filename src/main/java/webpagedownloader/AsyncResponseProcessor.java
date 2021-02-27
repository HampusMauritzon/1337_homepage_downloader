package webpagedownloader;

import webpagedownloader.client.HttpRequestHandler;
import webpagedownloader.parsing.ParsedWebsite;
import webpagedownloader.parsing.WebpageParser;

import java.net.URI;

public class AsyncResponseProcessor {
	private final HttpRequestHandler requestHandler;
	private final FileService fileService;

	public AsyncResponseProcessor(HttpRequestHandler requestHandler, FileService fileService) {
		this.requestHandler = requestHandler;
		this.fileService = fileService;
	}

	public void hyperlinkResponseHandling(URI link, String data) {
		fileService.storeFile(link, data);

		ParsedWebsite parsed = WebpageParser.parse(link, data);

		parsed.getHyperlinks().stream()
				.filter(fileService::notExists)
				.forEach(hyperlink -> {
					fileService.create(hyperlink);
					requestHandler.fetchWebpage(hyperlink, this::hyperlinkResponseHandling);
				});

		parsed.getAssets().stream()
				.filter(fileService::notExists)
				.forEach(asset -> {
					fileService.create(asset);
					requestHandler.fetchWebpage(asset, this::assetResponseHandling);
				});
	}

	public void assetResponseHandling(URI asset, String data) {
		fileService.storeFile(asset, data);
	}

}
