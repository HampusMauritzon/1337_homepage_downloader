package webpagedownloader.parsing;

import org.apache.commons.collections4.CollectionUtils;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class WebpageParserTest {

	@Test
	public void testParse() throws FileNotFoundException, URISyntaxException {
		File file = new File("src/test/testdata/testpage.html");
		BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
		String textToBeParsed = bufferedReader.lines().collect(Collectors.joining());

		ParsedWebsite actual = WebpageParser.parse(new URI("http://tretton37.com/"), textToBeParsed);

		Collection<URI> expectedHyperlink = Lists.list(new URI("http://tretton37.com/assets/we"),
				new URI("http://tretton37.com/pop"));
		assertEquals(actual.getHyperlinks().size(), expectedHyperlink.size());
		assertTrue(CollectionUtils.containsAll(actual.getHyperlinks(), expectedHyperlink));

		Collection<URI> expectedAssets = Lists.list(new URI("http://tretton37.com/script/testing.js"),
				new URI("http://tretton37.com/assets/images/kitty.jpg"),
				new URI("http://tretton37.com/assets/frack.css"));
		assertEquals(actual.getAssets().size(), expectedAssets.size());
		assertTrue(CollectionUtils.containsAll(actual.getAssets(), expectedAssets));
	}
}
