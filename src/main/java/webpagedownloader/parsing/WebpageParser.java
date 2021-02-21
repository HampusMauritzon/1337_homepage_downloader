package webpagedownloader.parsing;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.Collection;
import java.util.HashSet;

import static java.util.stream.Collectors.toSet;

public class WebpageParser {

	public static ParsedWebsite parse(String text) {
		Document doc = Jsoup.parse(text);
		return new ParsedWebsite(getHyperlinks(doc), getAssets(doc));
	}

	private static Collection<String> getHyperlinks(Document document) {
		Elements hyperlinks = document.select("a[href]");

		Collection<String> links = new HashSet<>();
		extractPaths(links, hyperlinks, "href");

		links = links.stream()
				.filter(link -> !isExternalLink(link))
				.map(WebpageParser::stripParameters)
				.filter(link -> !isNotLink(link))
				.collect(toSet());
		return links;
	}

	private static Collection<String> getAssets(Document document) {
		Elements links = document.select("link[href]");
		Elements images = document.select("img[src]");
		Elements videos = document.select("video[src]");
		Elements scripts = document.select("script[src]");

		Collection<String> assets = new HashSet<>();
		extractPaths(assets, links, "href");
		extractPaths(assets, images, "src");
		extractPaths(assets, videos, "src");
		extractPaths(assets, scripts, "src");

		assets = assets.stream()
				.filter(link -> !isExternalLink(link))
				.map(WebpageParser::stripParameters)
				.filter(link -> !isNotLink(link))
				.collect(toSet());
		return assets;
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
		return link.contains(":") || link.isEmpty();
	}

	private static String stripParameters(String link) {
		String[] strings = link.split("[#?]");
		return strings[0];
	}
}
