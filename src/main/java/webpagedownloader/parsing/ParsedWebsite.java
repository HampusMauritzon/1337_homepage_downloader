package webpagedownloader.parsing;

import java.util.Collection;

public class ParsedWebsite {
	private Collection<String> hyperlinks;
	private Collection<String> assets;

	public ParsedWebsite(Collection<String> hyperlinks, Collection<String> assets) {
		this.hyperlinks = hyperlinks;
		this.assets = assets;
	}

	public Collection<String> getHyperlinks() {
		return hyperlinks;
	}

	public Collection<String> getAssets() {
		return assets;
	}
}
