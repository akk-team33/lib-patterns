package de.team33.patterns.io.thalassa.publics;

import de.team33.patterns.io.thalassa.BytesIO;
import de.team33.patterns.io.thalassa.Input;
import de.team33.patterns.io.thalassa.Supply;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class BytesIOTest extends Supply {

    private static final Path PATH = Path.of("target", "testing", BytesIOTest.class.getSimpleName());
    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyz";
    private static final Input<byte[]> CLASSPATH_INPUT = BytesIO.by(BytesIOTest.class, "ResourceTest.txt");

    private final BytesIO bytesIO;
    private final Path path;

    BytesIOTest() {
        this.path = PATH.resolve("%s.txt".formatted(anyString(8, CHARACTERS)));
        this.bytesIO = BytesIO.by(path);
    }

    @Test
    final void by_classpath_read() throws IOException {
        final byte[] expected = ("p1=v1\n" +
                                 "p2=v2\n" +
                                 "p3=v3\n").getBytes(StandardCharsets.UTF_8);
        final byte[] result = CLASSPATH_INPUT.read();
        assertArrayEquals(expected, result);
    }

    @Test
    final void read() throws IOException {
        final byte[] original = anyString().getBytes(StandardCharsets.UTF_8);
        Files.createDirectories(path.getParent());
        Files.write(path, original);

        final byte[] result = bytesIO.read();
        assertArrayEquals(original, result);
    }

    @Test
    final void write() throws IOException {
        final byte[] original = anyString().getBytes(StandardCharsets.UTF_8);
        bytesIO.write(original);
        assertArrayEquals(original, bytesIO.read());
    }
}