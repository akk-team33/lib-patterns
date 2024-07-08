package de.team33.patterns.io.charon;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileTypeTest {

    private static final Path TEST_PATH = Paths.get("target",
                                                    "testing",
                                                    FileTypeTest.class.getSimpleName(),
                                                    UUID.randomUUID().toString());
    private static final Path MISSING_PATH = TEST_PATH.resolve("missing.file");
    private static final Path DIRECTORY_PATH = TEST_PATH.resolve("directory");
    private static final Path REGULAR_PATH = TEST_PATH.resolve("regular.file");
    private static final Path SYMBOLIC_PATH = TEST_PATH.resolve("symbolic.link");
    private static final Path SPECIAL_PATH = Paths.get("/dev/null");

    @BeforeAll
    static void beforeAll() throws IOException {
        Files.createDirectories(DIRECTORY_PATH);
        Files.write(REGULAR_PATH, "regular file content".getBytes(StandardCharsets.UTF_8));
        Files.createSymbolicLink(SYMBOLIC_PATH, DIRECTORY_PATH.getFileName());
    }

    private static BasicFileAttributes getAttributes(final Path path) {
        try {
            return Files.readAttributes(path, BasicFileAttributes.class, LinkOption.NOFOLLOW_LINKS);
        } catch (final NoSuchFileException ignore) {
            return null;
        } catch (final IOException e) {
            throw new AssertionError(e.getMessage(), e);
        }
    }

    @ParameterizedTest
    @EnumSource
    final void map(final MapCase testCase) {
        final BasicFileAttributes attributes = getAttributes(testCase.path);
        final FileType result = FileType.map(attributes);
        assertEquals(testCase.expected, result);
    }

    enum MapCase {

        MISSING(MISSING_PATH, FileType.MISSING),
        DIRECTORY(DIRECTORY_PATH, FileType.DIRECTORY),
        REGULAR(REGULAR_PATH, FileType.REGULAR),
        SYMBOLIC(SYMBOLIC_PATH, FileType.SYMBOLIC),
        SPECIAL(SPECIAL_PATH, FileType.SPECIAL)/**/;

        private final Path path;
        private final FileType expected;

        MapCase(final Path path, final FileType expected) {
            this.path = path;
            this.expected = expected;
        }
    }
}
