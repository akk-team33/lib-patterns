package de.team33.patterns.io.deimos.publics;

import de.team33.patterns.io.deimos.TextIO;
import de.team33.testing.io.hydra.FileIO;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class TextIOTest {

    private static final Path TEST_PATH = Paths.get("target", "testing", TextIOTest.class.getSimpleName())
                                               .toAbsolutePath()
                                               .normalize();
    private static final String TEXT_FILE = "TextIOTest.txt";
    private static final String MISSING_FILE = "missing.txt";
    private static final Path READ_PATH = TEST_PATH.resolve(TEXT_FILE);
    private static final Path MISSING_PATH = TEST_PATH.resolve(MISSING_FILE);

    @Test
    final void read_file() {
        FileIO.copy(getClass(), TEXT_FILE, READ_PATH);
        final String expected = String.format("bda815e0%n89ee%n4895%n8b9f%ncfa43be5684c");
        final String result = TextIO.read(READ_PATH);
        assertEquals(expected, result);
    }

    @Test
    final void read_file_missing() {
        try {
            final String result = TextIO.read(MISSING_PATH);
            fail("expected to fail - but was '" + result + "'");
        } catch (final IllegalArgumentException e) {
            // as expected, and ...
            assertTrue(e.getMessage().contains(MISSING_PATH.toString()));
        }
    }

    @Test
    final void read_resource() {
        final String expected = String.format("bda815e0%n89ee%n4895%n8b9f%ncfa43be5684c");
        final String result = TextIO.read(TextIOTest.class, TEXT_FILE);
        assertEquals(expected, result);
    }

    @Test
    final void read_resource_missing() {
        try {
            final String result = TextIO.read(getClass(), MISSING_FILE);
            fail("expected to fail - but was '" + result + "'");
        } catch (final IllegalArgumentException e) {
            // as expected, and ...
            final String message = e.getMessage();
            assertTrue(message.contains(getClass().getCanonicalName()));
            assertTrue(message.contains(MISSING_FILE));
        }
    }
}
