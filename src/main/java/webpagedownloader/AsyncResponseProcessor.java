package webpagedownloader;

import webpagedownloader.client.WebpageFetcher;
import webpagedownloader.parsing.ParsedWebsite;
import webpagedownloader.parsing.WebpageParser;

import java.net.URI;

public class AsyncResponseProcessor {
	private final WebpageFetcher webpageFetcher;
	private final FileService fileService;

	public AsyncResponseProcessor(WebpageFetcher webpageFetcher, FileService fileService) {
		this.webpageFetcher = webpageFetcher;
		this.fileService = fileService;
	}

	public void hyperlinkResponseHandling(URI link, String data) {
		fileService.storeFile(link, data);

		ParsedWebsite parsed = WebpageParser.parse(link, data);

		parsed.getHyperlinks().stream()
				.filter(fileService::notExists)
				.forEach(hyperlink -> {
					fileService.create(hyperlink);
					webpageFetcher.fetchWebpage(hyperlink, this::hyperlinkResponseHandling);
				});

		parsed.getAssets().stream()
				.filter(fileService::notExists)
				.forEach(asset -> {
					fileService.create(asset);
					webpageFetcher.fetchWebpage(asset, this::assetResponseHandling);
				});
	}

	public void assetResponseHandling(URI asset, String data) {
		fileService.storeFile(asset, data);
	}

}
