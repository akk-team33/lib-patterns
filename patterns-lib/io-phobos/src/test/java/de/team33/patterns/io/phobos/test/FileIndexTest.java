package de.team33.patterns.io.phobos.test;

import de.team33.patterns.io.deimos.TextIO;
import de.team33.patterns.io.phobos.FileEntry;
import de.team33.patterns.io.phobos.FileIndex;
import de.team33.patterns.testing.titan.io.ZipIO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class FileIndexTest {

    private static final Path TEST_PATH = Paths.get("target", "testing", FileIndexTest.class.getSimpleName());
    private static final Path CWD = Paths.get(".").toAbsolutePath().normalize();
    private static final String NEW_LINE = String.format("%n");

    @BeforeAll
    static void init() {
        ZipIO.unzip(FileIndexTest.class, "files.zip", TEST_PATH);
    }

    @Test
    final void skipPath() {
        final String expected = TextIO.read(getClass(), "FileIndexTest.skipPath.txt");
        final String result = collected(FileIndex.of(TEST_PATH.toString())
                                                 .skipPath(entry -> "main".equals(entry.getFileName().toString()))
                                                 .stream());
        assertEquals(expected, result);
    }

    @Test
    final void skipEntry() {
        final String expected = TextIO.read(getClass(), "FileIndexTest.skipEntry.txt");
        final String result = collected(FileIndex.of(TEST_PATH)
                                                 .skipEntry(entry -> "test".equals(entry.name()))
                                                 .stream());
        assertEquals(expected, result);
    }

    @Test
    final void stream() {
        final String expected = TextIO.read(getClass(), "FileIndexTest.stream.txt");
        final String result = collected(FileIndex.of(TEST_PATH.toString())
                                                 .stream());
        assertEquals(expected, result);
    }

    @Test
    final void testToString() {
        final String expected = TextIO.read(getClass(), "FileIndexTest.toString.txt");
        final String result = FileIndex.of(TEST_PATH).toString();
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
