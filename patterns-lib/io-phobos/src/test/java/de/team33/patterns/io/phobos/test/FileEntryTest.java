package de.team33.patterns.io.phobos.test;

import de.team33.patterns.io.phobos.FileEntry;
import de.team33.patterns.io.phobos.FileType;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileEntryTest {

    private static final Path DEV_NULL = Paths.get("/", "dev", "null"); // special file
    private static final Path ROOT = Paths.get("/", "root"); // unreadable directory

    static Stream<Path> paths() {
        return Stream.of(
                Paths.get("file", "is", "missing"),
                Paths.get("src", "main", "java"),
                Paths.get("pom.xml"),
                DEV_NULL,
                ROOT);
    }

    @ParameterizedTest
    @MethodSource("paths")
    final void path(final Path path) {
        final FileEntry entry = FileEntry.of(path);
        assertTrue(entry.path().isAbsolute());
    }

    @ParameterizedTest
    @MethodSource("paths")
    final void name(final Path path) {
        final FileEntry entry = FileEntry.of(path);
        assertEquals(path.getFileName().toString(), entry.name());
    }

    @ParameterizedTest
    @MethodSource("paths")
    final void type(final Path path) {
        final FileEntry entry = FileEntry.of(path);
        if (entry.isDirectory()) {
            assertEquals(FileType.DIRECTORY, entry.type());
        } else if (entry.isSymbolicLink()) {
            assertEquals(FileType.SYMBOLIC, entry.type());
        } else if (entry.isRegularFile()) {
            assertEquals(FileType.REGULAR, entry.type());
        } else if (entry.isOther()) {
            assertEquals(FileType.OTHER, entry.type());
        } else {
            assertEquals(FileType.MISSING, entry.type());
        }
    }

    @ParameterizedTest
    @MethodSource("paths")
    final void isDirectory(final Path path) {
        final FileEntry entry = FileEntry.of(path);
        assertEquals(Files.isDirectory(path), entry.isDirectory());
    }

    @ParameterizedTest
    @MethodSource("paths")
    final void isRegularFile(final Path path) {
        final FileEntry entry = FileEntry.of(path);
        assertEquals(Files.isRegularFile(path), entry.isRegularFile());
    }

    @ParameterizedTest
    @MethodSource("paths")
    final void isSymbolicLink(final Path path) {
        final FileEntry entry = FileEntry.of(path, LinkOption.NOFOLLOW_LINKS);
        assertEquals(Files.isSymbolicLink(path), entry.isSymbolicLink());
    }

    @ParameterizedTest
    @MethodSource("paths")
    final void isOther(final Path path) {
        final FileEntry entry = FileEntry.of(path);
        if (entry.isOther()) {
            assertEquals(DEV_NULL, path);
        } else {
            assertNotEquals(DEV_NULL, path);
        }
    }

    @ParameterizedTest
    @MethodSource("paths")
    final void exists(final Path path) {
        final FileEntry entry = FileEntry.of(path);
        assertEquals(Files.exists(path), entry.exists());
    }

    @ParameterizedTest
    @MethodSource("paths")
    final void lastModified(final Path path) throws IOException {
        final FileEntry entry = FileEntry.of(path);
        if (entry.exists()) {
            assertEquals(Files.getLastModifiedTime(path).toInstant(), entry.lastModified());
        } else {
            assertThrows(UnsupportedOperationException.class, entry::lastModified);
        }
    }

    @ParameterizedTest
    @MethodSource("paths")
    final void lastAccess(final Path path) {
        final FileEntry entry = FileEntry.of(path);
        if (entry.exists()) {
            assertNotNull(entry.lastAccess());
        } else {
            assertThrows(UnsupportedOperationException.class, entry::lastAccess);
        }
    }

    @ParameterizedTest
    @MethodSource("paths")
    final void creation(final Path path) {
        final FileEntry entry = FileEntry.of(path);
        if (entry.exists()) {
            assertNotNull(entry.creation());
        } else {
            assertThrows(UnsupportedOperationException.class, entry::creation);
        }
    }

    @ParameterizedTest
    @MethodSource("paths")
    final void size(final Path path) throws IOException {
        final FileEntry entry = FileEntry.of(path);
        if (entry.exists()) {
            assertEquals(Files.size(path), entry.size());
        } else {
            assertThrows(UnsupportedOperationException.class, entry::size);
        }
    }

    @ParameterizedTest
    @MethodSource("paths")
    final void content(final Path path) {
        final FileEntry entry = FileEntry.of(path);
        if (entry.isDirectory()) {
            assertNotNull(entry.content());
        } else {
            assertThrows(UnsupportedOperationException.class, entry::content);
        }
    }
}
