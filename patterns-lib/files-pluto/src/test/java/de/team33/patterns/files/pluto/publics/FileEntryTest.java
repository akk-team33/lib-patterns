package de.team33.patterns.files.pluto.publics;

import de.team33.patterns.files.pluto.FileEntry;
import de.team33.patterns.files.pluto.TUtil;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class FileEntryTest {

    private static final String CLASS_NAME = FileEntryTest.class.getSimpleName();
    @SuppressWarnings("HardcodedFileSeparator")
    private static final Path TEST_PATH = Path.of("target", "testing", CLASS_NAME);
    @SuppressWarnings("HardcodedFileSeparator")
    private static final Path DEV_NULL = Paths.get("/dev/null"); // special file
    @SuppressWarnings("HardcodedFileSeparator")
    private static final Path ROOT_HOME = Paths.get("/root"); // unreadable directory (Linux)
    @SuppressWarnings("HardcodedFileSeparator")
    private static final Path ROOT = Paths.get("/"); // root directory

    private final String uuid = UUID.randomUUID().toString();
    private final Path testPath = TEST_PATH.resolve(uuid);
    private final Path missingLink = testPath.resolve("missing.link");
    private final Path dirLink = testPath.resolve("directory.link");
    private final Path regularLink = testPath.resolve("regular.link");
    private final Path specialLink = testPath.resolve("special.link");
    private final Path linkLink = testPath.resolve("link.link");
    private final Path missingFile = testPath.resolve("file/is/missing");
    private final Path directory = testPath;
    private final Path regularFile = directory.resolve("regular.file");

    FileEntryTest() throws IOException {
        Files.createDirectories(testPath);
        Files.writeString(regularFile, UUID.randomUUID().toString());
        Files.createSymbolicLink(missingLink, missingFile.toAbsolutePath().normalize());
        Files.createSymbolicLink(dirLink, directory.toAbsolutePath().normalize());
        Files.createSymbolicLink(regularLink, regularFile.toAbsolutePath().normalize());
        Files.createSymbolicLink(specialLink, DEV_NULL);
        Files.createSymbolicLink(linkLink, regularLink.toAbsolutePath().normalize());
    }

    private static String nameOf(final Path path) {
        return Optional.ofNullable(path.getFileName()).orElse(path).toString();
    }

    private static BasicFileAttributes readAttributes(final Path path, final LinkOption[] options) {
        try {
            return Files.readAttributes(path, BasicFileAttributes.class, options);
        } catch (IOException e) {
            return TUtil.MISSING_FILE_ATTRIBUTES;
        }
    }

    final List<Path> paths() {
        return List.of(
                missingFile,
                directory,
                regularFile,
                missingLink,
                dirLink,
                regularLink,
                specialLink,
                linkLink,
                DEV_NULL,
                ROOT_HOME,
                ROOT);
    }

    @Test
    final void path() {
        paths().forEach(path -> {
            final FileEntry entry = FileEntry.original(path);
            assertTrue(entry.path().isAbsolute());
        });
    }

    @Test
    final void name() {
        paths().forEach(path -> {
            final FileEntry entry = FileEntry.original(path);
            assertEquals(nameOf(path), entry.name());
        });
    }

    @Test
    final void testToString() {
        paths().forEach(path -> {
            final FileEntry entry = FileEntry.original(path);
            assertEquals(path.toAbsolutePath().normalize().toString(), entry.toString());
        });
    }

    @Test
    final void isDirectory() {
        paths().forEach(path -> {
            final FileEntry entry = FileEntry.resolved(path);
            assertEquals(Files.isDirectory(path), entry.isDirectory());
        });
    }

    @Test
    final void isRegularFile() {
        paths().forEach(path -> {
            final FileEntry entry = FileEntry.resolved(path);
            assertEquals(Files.isRegularFile(path), entry.isRegularFile());
        });
    }

    @Test
    final void isSymbolicLink() {
        paths().forEach(path -> {
            final FileEntry entry = FileEntry.original(path);
            assertEquals(Files.isSymbolicLink(path), entry.isSymbolicLink());
        });
    }

    @Test
    final void isSpecialFile() {
        for (final Path path : paths()) {
            // System.out.println(path);
            final boolean expected = readAttributes(path, TUtil.RESOLVE_LINKS).isOther();
            final FileEntry entry = FileEntry.resolved(path);
            assertEquals(expected, entry.isSpecialFile());
        }
    }

    @Test
    final void isMissing() {
        paths().forEach(path -> {
            // System.out.println(path);
            final FileEntry entry = FileEntry.resolved(path);
            assertEquals(!Files.exists(path, TUtil.RESOLVE_LINKS), entry.isMissing());
        });
    }

    @Test
    final void isPresent() {
        paths().forEach(path -> {
            // System.out.println(path);
            final FileEntry entry = FileEntry.original(path);
            assertEquals(Files.exists(path, TUtil.ORIGINAL_LINKS), entry.isPresent());
        });
    }

    @Test
    final void lastModified() {
        for (final Path path : paths()) {
            // System.out.println(path);
            final FileEntry entry = FileEntry.resolved(path);
            try {
                final Instant result = entry.lastModified();
                assertFalse(entry.isMissing());
                final Instant expected = readAttributes(path, TUtil.RESOLVE_LINKS).lastModifiedTime()
                                                                                  .toInstant();
                assertEquals(expected, result);
            } catch (final UnsupportedOperationException caught) {
                assertTrue(entry.isMissing());
                assertEquals(entry.isSymbolicLink(), entry.isPresent());
            }
        }
    }

    @Test
    final void lastAccess() {
        for (final Path path : paths()) {
            // System.out.println(path);
            final FileEntry entry = FileEntry.resolved(path);
            try {
                final Instant result = entry.lastAccess();
                assertFalse(entry.isMissing());
                final Instant expected = readAttributes(path, TUtil.RESOLVE_LINKS).lastAccessTime()
                                                                                  .toInstant();
                assertEquals(expected, result);
            } catch (final UnsupportedOperationException caught) {
                assertTrue(entry.isMissing());
                assertEquals(entry.isSymbolicLink(), entry.isPresent());
            }
        }
    }

    @Test
    final void creation() {
        for (final Path path : paths()) {
            // System.out.println(path);
            final FileEntry entry = FileEntry.original(path);
            try {
                final Instant result = entry.creation();
                assertTrue(entry.isPresent());
                final Instant expected = readAttributes(path, TUtil.ORIGINAL_LINKS).creationTime()
                                                                                   .toInstant();
                assertEquals(expected, result);
            } catch (final UnsupportedOperationException caught) {
                assertTrue(entry.isMissing());
                assertFalse(entry.isSymbolicLink());
            }
        }
    }

    @Test
    final void size() throws IOException {
        for (final Path path : paths()) {
            final FileEntry entry = FileEntry.resolved(path);
            if (entry.isMissing()) {
                assertEquals(0L, entry.size());
            } else {
                assertEquals(Files.size(path), entry.size());
            }
        }
    }

    @Test
    final void isOriginal() {
        paths().forEach(path -> {
            final FileEntry entry = FileEntry.original(path);
            assertTrue(entry.isOriginal());
            assertEquals(!Files.isSymbolicLink(path), entry.isResolved());
        });
    }

    @Test
    final void original() {
        paths().forEach(path -> {
            final FileEntry entry = FileEntry.resolved(path).original();
            assertTrue(entry.isOriginal());
            assertEquals(!Files.isSymbolicLink(path), entry.isResolved());
        });
    }

    @Test
    final void isResolved() {
        paths().forEach(path -> {
            final FileEntry entry = FileEntry.resolved(path);
            assertTrue(entry.isResolved());
            assertEquals(!Files.isSymbolicLink(path), entry.isOriginal());
        });
    }

    @Test
    final void resolved() {
        paths().forEach(path -> {
            final FileEntry entry = FileEntry.original(path).resolved();
            assertTrue(entry.isResolved());
            assertEquals(!Files.isSymbolicLink(path), entry.isOriginal());
        });
    }
}