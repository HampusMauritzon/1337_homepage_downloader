package webpagedownloader.parsing;

import org.apache.commons.collections4.CollectionUtils;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import webpagedownloader.parsing.ParsedWebsite;
import webpagedownloader.parsing.WebpageParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Collection;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class WebpageParserTest {

	@Test
	public void testParse() throws FileNotFoundException {
		File file = new File("src/test/testdata/testpage.html");
		BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
		String textToBeParsed = bufferedReader.lines().collect(Collectors.joining());

		ParsedWebsite actual = WebpageParser.parse(textToBeParsed);

		Collection<String> expectedHyperlink = Lists.list("/assets/we", "tretton37.com/pop");
		assertEquals(actual.getHyperlinks().size(), expectedHyperlink.size());
		assertTrue(CollectionUtils.containsAll(actual.getHyperlinks(), expectedHyperlink));

		Collection<String> expectedAssets = Lists.list("/script/testing.js", "/assets/images/kitty.jpg", "/assets/frack.css");
		assertEquals(actual.getAssets().size(), expectedAssets.size());
		assertTrue(CollectionUtils.containsAll(actual.getAssets(), expectedAssets));
	}
}
