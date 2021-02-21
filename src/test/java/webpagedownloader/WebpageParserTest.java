package webpagedownloader;

import org.apache.commons.collections4.CollectionUtils;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

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

		Collection<String> actual = WebpageParser.parse(textToBeParsed);

		Collection<String> expected = Lists.list("/assets/we", "tretton37.com/pop");
		assertEquals(actual.size(), expected.size());
		assertTrue(CollectionUtils.containsAll(actual, expected));
	}
}
