package de.team33.patterns.io.charon.publics;

import de.team33.patterns.io.charon.FileEntry;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class FileEntryTest {

    private static final Path TEST_PATH = Paths.get("target",
                                                    "testing",
                                                    FileEntryTest.class.getSimpleName(),
                                                    UUID.randomUUID().toString());

    @Test
    void path() {
        final FileEntry entry = FileEntry.of(TEST_PATH);
        assertEquals(TEST_PATH.toAbsolutePath().normalize(), entry.path());
    }

    @Test
    void name() {
        final FileEntry entry = FileEntry.of(TEST_PATH);
        assertEquals(TEST_PATH.getFileName().toString(), entry.name());
    }
}
