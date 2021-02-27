package webpagedownloader;

import org.apache.commons.collections4.CollectionUtils;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import webpagedownloader.client.HttpRequestHandler;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AsyncResponseProcessorTest {
	private AsyncResponseProcessor asyncResponseProcessor;

	private HttpRequestHandler httpRequestHandler;
	private FileService fileService;

	@BeforeEach
	public void setup() {
		httpRequestHandler = mock(HttpRequestHandler.class);
		fileService = mock(FileService.class);

		asyncResponseProcessor = new AsyncResponseProcessor(httpRequestHandler, fileService);
	}

	@Test
	public void testAssetResponseHandling() throws URISyntaxException {
		URI url = new URI("http://tretton37.com/asset.js");
		String data = "this is som asset data.";

		asyncResponseProcessor.assetResponseHandling(url, data);
		verify(fileService).storeFile(url, data);
	}

	@Test
	public void testHyperlinkResponseHandling() throws URISyntaxException {
		when(fileService.notExists(any())).thenReturn(true);

		URI parentUrl = new URI("http://tretton37.com/folder/");
		String data = "this is som asset data. <a href=\"subfolder/xena\">" +
				"<a href=\"/parallell/concure\">";

		asyncResponseProcessor.hyperlinkResponseHandling(parentUrl, data);

		ArgumentCaptor<URI> hyperLinkCaptor = ArgumentCaptor.forClass(URI.class);
		ArgumentCaptor<URI> assetCaptor = ArgumentCaptor.forClass(URI.class);
		verify(httpRequestHandler, times(2)).fetchWebpage(hyperLinkCaptor.capture(), any());

		List<URI> actual = hyperLinkCaptor.getAllValues();
		Collection<URI> expected = Lists.list(new URI("http://tretton37.com/folder/subfolder/xena"),
				new URI("http://tretton37.com/parallell/concure"));
		assertEquals(actual.size(), expected.size());
		assertTrue(CollectionUtils.containsAll(actual, expected));
	}
}
