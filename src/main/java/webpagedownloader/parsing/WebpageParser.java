package webpagedownloader.parsing;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.yaml.snakeyaml.util.UriEncoder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public class WebpageParser {

	public static ParsedWebsite parse(URI page, String text) {
		if (text == null) {
			return new ParsedWebsite();
		}
		Document doc = Jsoup.parse(text);
		return new ParsedWebsite(getHyperlinks(page, doc), getAssets(page, doc));
	}

	private static Set<URI> getHyperlinks(URI page, Document document) {
		Elements hyperlinks = document.select("a[href]");

		Collection<String> links = new HashSet<>();
		extractPaths(links, hyperlinks, "href");

		return resolveLinks(page, links);
	}

	private static Set<URI> getAssets(URI page, Document document) {
		Elements linkers = document.select("link[href]");
		Elements images = document.select("img[src]");
		Elements videos = document.select("video[src]");
		Elements scripts = document.select("script[src]");

		Collection<String> links = new HashSet<>();
		extractPaths(links, linkers, "href");
		extractPaths(links, images, "src");
		extractPaths(links, videos, "src");
		extractPaths(links, scripts, "src");

		return resolveLinks(page, links);
	}

	private static Set<URI> resolveLinks(URI currentPage, Collection<String> links) {
		return links.stream()
				.filter(link -> !isNotHTTP(link))
				.map(WebpageParser::removeParameters)
				.map(link -> resolveBacktracking(currentPage, link))
				.filter(link -> link.getHost() != null)
				.map(WebpageParser::parseHost)
				.filter(WebpageParser::isInternalLink)
				.collect(toSet());
	}

	private static void extractPaths(Collection<String> resources, Elements elements, String attributeKey) {
		elements.forEach(hyperlink ->
				resources.add(hyperlink.attr(attributeKey)));
	}

	private static boolean isInternalLink(URI link) {
		return link.getHost().contains("tretton37.com");
	}

	private static boolean isNotHTTP(String link) {
		return link.contains("mailto:") || link.isEmpty();
	}

	private static URI resolveBacktracking(URI page, String link) {
		String[] parts = UriEncoder.encode(link).split("/\\.\\./");
		page = page.resolve(parts[0]);
		for (int i = 1; i < parts.length; i++) {
			page = page.resolve("../" + parts[i]);
		}
		return page;
	}

	private static String removeParameters(String link) {
		return link.split("[?#]")[0];
	}

	private static URI parseHost(URI filePath) {
		String host = filePath.getHost();
		if (host.contains("tretton37.com")) {
			String[] parts = host.split("\\.");
			StringBuilder sb = new StringBuilder();
			if (parts.length > 2) {
				if (!parts[0].equals("www")) {
					for (int i = 0; i < parts.length - 2; i++) {
						sb.append("/");
						sb.append(parts[i]);
					}
				}
				try {
					return new URI("https://tretton37.com" + sb.toString() + filePath.getPath());
				} catch (URISyntaxException e) {
					e.printStackTrace();
				}
			}
		}
		return filePath;
	}
}
