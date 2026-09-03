package de.team33.patterns.io.thalassa.publics;

import de.team33.patterns.io.thalassa.Reading;
import de.team33.patterns.io.thalassa.Supply;
import de.team33.patterns.io.thalassa.Writing;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WritingTest extends Supply {

    private static final Path PATH = Path.of("target", "testing", WritingTest.class.getSimpleName());
    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyz";

    private final Writing writing;
    private final Reading reading;
    private final Path path;

    WritingTest() throws IOException {
        Files.createDirectories(PATH);
        this.path = PATH.resolve("%s.txt".formatted(anyString(8, CHARACTERS)));
        this.writing = () -> Files.newOutputStream(path);
        this.reading = () -> Files.newInputStream(path);
    }

    private static String readString(final BufferedReader in) throws IOException {
        try (final StringWriter out = new StringWriter()) {
            in.transferTo(out);
            return out.toString();
        }
    }

    private static void writeString(final BufferedWriter out, final String string) throws IOException {
        out.write(string);
    }

    private static String inputString(final InputStream in) throws IOException {
        return new String(in.readAllBytes(), StandardCharsets.UTF_8);
    }

    private static void outputString(final OutputStream out, final String text) throws IOException {
        out.write(text.getBytes(StandardCharsets.UTF_8));
    }

    @Test
    final void output_bytes_write() throws IOException {
        final String original = anyString();

        writing.output(WritingTest::outputString)
               .write(original);

        assertEquals(original, reading.input(WritingTest::inputString).read());
    }

    @Test
    final void output_chars_write() throws IOException {
        final String original = anyString();

        writing.output(StandardCharsets.UTF_8, WritingTest::writeString)
               .write(original);

        assertEquals(original, reading.input(StandardCharsets.UTF_8, WritingTest::readString).read());
    }
}