package de.team33.patterns.io.thalassa.publics;

import de.team33.patterns.io.thalassa.Input;
import de.team33.patterns.io.thalassa.Supply;
import de.team33.patterns.io.thalassa.TextIO;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TextIOTest extends Supply {

    private static final Path PATH = Path.of("target", "testing", TextIOTest.class.getSimpleName());
    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyz";
    private static final Input<String> CLASSPATH_INPUT = TextIO.by(TextIOTest.class, "ResourceTest.txt");

    private final TextIO textIO;
    private final Path path;

    TextIOTest() {
        this.path = PATH.resolve("%s.txt".formatted(anyString(8, CHARACTERS)));
        this.textIO = TextIO.by(path);
    }

    @Test
    final void by_classpath_read() throws IOException {
        final String expected = "p1=v1\n" +
                                "p2=v2\n" +
                                "p3=v3\n";
        final String result = CLASSPATH_INPUT.read();
        assertEquals(expected, result);
    }

    @Test
    final void read() throws IOException {
        final String original = anyString();
        Files.createDirectories(path.getParent());
        Files.writeString(path, original);

        final String result = textIO.read();
        assertEquals(original, result);
    }

    @Test
    final void write() throws IOException {
        final String original = anyString();
        textIO.write(original);
        assertEquals(original, textIO.read());
    }
}