package de.team33.patterns.io.phobos.test;

import de.team33.patterns.io.deimos.TextIO;
import de.team33.patterns.io.phobos.FileEntry;
import de.team33.patterns.io.phobos.FileIndex;
import de.team33.patterns.io.phobos.FilePolicy;
import de.team33.testing.io.hydra.ZipIO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class FileIndexTest {

    private static final Path TEST_PATH = Paths.get("target", "testing", FileIndexTest.class.getSimpleName());
    private static final String NEW_LINE = String.format("%n");

    @BeforeAll
    static void init() {
        ZipIO.unzip(FileIndexTest.class, "files.zip", TEST_PATH);
    }

    @Test
    final void skipPath() {
        final String expected = TextIO.read(getClass(), "FileIndexTest.skipPath.txt");
        final String result = collected(FileIndex.of(TEST_PATH, FilePolicy.DISTINCT_SYMLINKS)
                                                 .skipPath(entry -> "main".equals(entry.getFileName().toString()))
                                                 .entries());
        assertEquals(expected, result);
    }

    @Test
    final void skipEntry() {
        final String expected = TextIO.read(getClass(), "FileIndexTest.skipEntry.txt");
        final String result = collected(FileIndex.of(TEST_PATH, FilePolicy.RESOLVE_SYMLINKS)
                                                 .skipEntry(entry -> "test".equals(entry.name()))
                                                 .entries());
        assertEquals(expected, result);
    }

    @Test
    final void stream() {
        final String expected = TextIO.read(getClass(), "FileIndexTest.stream.txt");
        final String result = collected(FileIndex.of(TEST_PATH, FilePolicy.RESOLVE_SYMLINKS)
                                                 .entries());
        assertEquals(expected, result);
    }

    private static String collected(final Stream<FileEntry> entrys) {
        final Path normal = TEST_PATH.toAbsolutePath().normalize();
        return entrys.map(FileEntry::path)
                     .map(normal::relativize)
                     .map(Path::toString)
                     .collect(Collectors.joining(NEW_LINE));
    }
}
