package webpagedownloader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.Collection;
import java.util.HashSet;

import static java.util.stream.Collectors.toSet;

public class WebpageParser {
	public static Collection<String> parse(String text) {
		Document doc = Jsoup.parse(text);
		Elements hyperlinks = doc.select("a[href]");
		//Elements links = doc.select("link[href]");
		//Elements images = doc.select("img[src]");
		//Elements videos = doc.select("video[src]");
		//Elements scripts = doc.select("script[src]");

		Collection<String> resources = new HashSet<>();
		extractPaths(resources, hyperlinks, "href");
		//extractPaths(resources, links, "href");
		//extractPaths(resources, images, "src");
		//extractPaths(resources, videos, "src");
		//extractPaths(resources, scripts, "src");

		resources = resources.stream()
				.filter(link -> !isExternalLink(link))
				.map(WebpageParser::stripParameters)
				.filter(link -> !isNotLink(link))
				.collect(toSet());
		return resources;
	}

	private static void extractPaths(Collection<String> resources, Elements elements, String attributeKey) {
		elements.forEach(hyperlink -> {
			String path = hyperlink.attr(attributeKey);
			resources.add(path);
		});
	}

	private static boolean isExternalLink(String link) {
		return link.contains(".net") ||
				link.contains(".se") ||
				(link.contains(".com") && !link.contains("tretton37.com"));
	}

	private static boolean isNotLink(String link) {
		return link.contains("javascript:") || link.isEmpty();
	}

	private static String stripParameters(String link) {
		String[] strings = link.split("[#?]");
		return strings[0];
	}
}
