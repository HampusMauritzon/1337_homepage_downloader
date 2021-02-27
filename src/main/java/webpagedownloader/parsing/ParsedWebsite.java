package webpagedownloader.parsing;

import java.net.URI;
import java.util.Collection;
import java.util.Collections;

public class ParsedWebsite {
	private final Collection<URI> hyperlinks;
	private final Collection<URI> assets;

	public ParsedWebsite() {
		this.hyperlinks = Collections.emptyList();
		this.assets = Collections.emptyList();
	}

	public ParsedWebsite(Collection<URI> hyperlinks, Collection<URI> assets) {
		this.hyperlinks = hyperlinks;
		this.assets = assets;
	}

	public Collection<URI> getHyperlinks() {
		return hyperlinks;
	}

	public Collection<URI> getAssets() {
		return assets;
	}
}
