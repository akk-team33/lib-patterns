package de.team33.patterns.io.thalassa.publics;

import de.team33.patterns.io.thalassa.Input;
import de.team33.patterns.io.thalassa.Reading;
import de.team33.patterns.io.thalassa.Supply;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ReadingTest extends Supply {

    private static final Reading CLASSPATH_READING =
            () -> ReadingTest.class.getResourceAsStream("ResourceTest.txt");

    private static String readString(final BufferedReader in) throws IOException {
        try (final StringWriter out = new StringWriter()) {
            in.transferTo(out);
            return out.toString();
        }
    }

    @Test
    final void by_classpath_read() throws IOException {
        final String expected = "p1=v1\n" +
                                "p2=v2\n" +
                                "p3=v3\n";
        final Input<String> input = CLASSPATH_READING.input(StandardCharsets.UTF_8, ReadingTest::readString);

        final String result = input.read();
        assertEquals(expected, result);
    }

    @Test
    final void input_bytes_read() throws IOException {
        final byte[] original = anyString().getBytes(StandardCharsets.UTF_8);
        final Reading reading = () -> new ByteArrayInputStream(original);
        final Input<byte[]> input = reading.input(InputStream::readAllBytes);

        final byte[] result = input.read();
        assertArrayEquals(original, result);
    }

    @Test
    final void input_chars_read() throws IOException {
        final String original = anyString();
        final byte[] bytes = original.getBytes(StandardCharsets.UTF_8);
        final Reading resource = () -> new ByteArrayInputStream(bytes);
        final Input<String> input = resource.input(StandardCharsets.UTF_8, ReadingTest::readString);

        final String result = input.read();
        assertEquals(original, result);
    }
}