package de.team33.patterns.io.thalassa.publics;

import de.team33.patterns.collection.ceres.Mapping;
import de.team33.patterns.io.thalassa.Input;
import de.team33.patterns.io.thalassa.PropertiesIO;
import de.team33.patterns.io.thalassa.Supply;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PropertiesIOTest extends Supply {

    private static final Path PATH = Path.of("target", "testing", PropertiesIOTest.class.getSimpleName());
    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyz";
    private static final Input<Properties> CLASSPATH_INPUT =
            PropertiesIO.by(PropertiesIOTest.class, "ResourceTest.txt");

    private final PropertiesIO io;
    private final Path path;

    PropertiesIOTest() throws IOException {
        this.path = PATH.resolve("%s.txt".formatted(anyString(8, CHARACTERS)));
        this.io = PropertiesIO.by(path);
    }

    @Test
    final void by_classpath_read() throws IOException {
        final Properties expected = Mapping.builder(Properties::new)
                                           .put("p1", "v1")
                                           .put("p2", "v2")
                                           .put("p3", "v3")
                                           .build();
        final Properties result = CLASSPATH_INPUT.read();
        assertEquals(expected, result);
    }

    @Test
    final void read() throws IOException {
        final Properties original = CLASSPATH_INPUT.read();
        Files.createDirectories(path.getParent());
        try (final Writer out = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            original.store(out, "comment");
        }

        final Properties result = io.read();
        assertEquals(original, result);
    }

    @Test
    final void write() throws IOException {
        final Properties original = CLASSPATH_INPUT.read();
        io.write(original);
        assertEquals(original, io.read());
    }
}