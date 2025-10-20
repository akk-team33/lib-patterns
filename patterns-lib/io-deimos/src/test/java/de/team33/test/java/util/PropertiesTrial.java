package de.team33.test.java.util;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Properties;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PropertiesTrial {

    private static final Pattern COMMENT_PATTERN = Pattern.compile("#.*");
    private static final String LOREM_IPSUM = String.format(
            "Lorem ipsum dolor sit amet, consetetur sadipscing elitr,%n" +
            "sed diam nonumy eirmod tempor invidunt ut labore%n" +
            "et dolore magna aliquyam erat, sed diam voluptua.%n" +
            "At vero eos et accusam et justo duo dolores et ea rebum.%n" +
            "Stet clita kasd gubergren, no sea takimata sanctus est.%n");

    @Test
    final void complexValues() throws IOException {
        final Properties expected = new Properties() {{
            put("simpleKey", "simpleValue");
            put("key with whitespace", "value with whitespace");
            put("key-with-hyphen", "value\twith\t\"tabs\"\tand\"quotation mark\"");
            put("multiline-text", LOREM_IPSUM);
            put("leading.and.trailing.whitespace", "  value with leading\r\nand trailing whitespace   ");
        }};

        final StringWriter writer = new StringWriter();
        expected.store(writer, null);
        final String stage = COMMENT_PATTERN.matcher(writer.toString()).replaceAll("");
        // System.out.println(stage);

        final StringReader reader = new StringReader(stage);
        final Properties result = new Properties();
        result.load(reader);

        assertEquals(expected, result);
    }
}
