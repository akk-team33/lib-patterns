package de.team33.patterns.io.charon.publics;

import de.team33.patterns.io.charon.FileEntry;
import de.team33.patterns.io.charon.FileType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class FileEntryTest {

    private static final Path TEST_PATH = Paths.get("target",
                                                    "testing",
                                                    FileEntryTest.class.getSimpleName(),
                                                    UUID.randomUUID().toString());
    private static final Path MISSING_FILE_PATH = TEST_PATH.resolve("missing.file");
    private static final Path MISSING_PARENT_PATH = Paths.get("missing", "parent", "path");
    private static final Path DIRECTORY_PATH = TEST_PATH.resolve("directory");
    private static final Path REGULAR_PATH = TEST_PATH.resolve("regular.file");
    private static final Path SYMBOLIC_PATH = TEST_PATH.resolve("symbolic.link");

    @BeforeAll
    static void beforeAll() throws IOException {
        Files.createDirectories(DIRECTORY_PATH);
        Files.write(REGULAR_PATH, "regular file content".getBytes(StandardCharsets.UTF_8));
        Files.createSymbolicLink(SYMBOLIC_PATH, DIRECTORY_PATH.getFileName());
    }

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

    @ParameterizedTest
    @EnumSource
    void type(final TypeCase testCase) {
        final FileEntry entry = FileEntry.of(testCase.path);
        assertEquals(testCase.expected, entry.type());
    }

    enum TypeCase {
        MISSING_FILE(MISSING_FILE_PATH, FileType.MISSING),
        MISSING_PARENT(MISSING_PARENT_PATH, FileType.MISSING),
        REGULAR(REGULAR_PATH, FileType.REGULAR),
        DIRECTORY(DIRECTORY_PATH, FileType.DIRECTORY),
        SYMBOLIC(SYMBOLIC_PATH, FileType.SYMBOLIC);

        private final Path path;
        private final FileType expected;

        TypeCase(final Path path, final FileType expected) {
            this.path = path;
            this.expected = expected;
        }
    }
}
