package de.team33.patterns.io.phobos.test;

import de.team33.patterns.io.deimos.TextIO;
import de.team33.patterns.io.phobos.FileEntry;
import de.team33.patterns.io.phobos.FileIndex;
import de.team33.patterns.io.phobos.testing.IoTestBase;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileIndexTest extends IoTestBase {

    private static final String NEW_LINE = String.format("%n");

    @Test
    final void skipPath() {
        final String expected = TextIO.read(getClass(), "FileIndexTest.skipPath.txt");
        final String result = collected(FileIndex.of(testPath())
                                                 .skipPath(entry -> "main".equals(entry.getFileName().toString()))
                                                 .entries());
        assertEquals(expected, result);
    }

    @Test
    final void skipEntry() {
        final String expected = TextIO.read(getClass(), "FileIndexTest.skipEntry.txt");
        final String result = collected(FileIndex.of(testPath())
                                                 .skipEntry(entry -> "test".equals(entry.name()))
                                                 .entries());
        assertEquals(expected, result);
    }

    @Test
    final void entries_distinct() {
        final String expected = TextIO.read(getClass(), "FileIndexTest.stream_distinct.txt");
        final String result = collected(FileIndex.of(testPath())
                                                 .entries());
        assertEquals(expected, result);
    }

    @Test
    final void entries_resolved() {
        final String expected = TextIO.read(getClass(), "FileIndexTest.stream_resolved.txt");
        final String result = collected(FileIndex.of(testPath())
                                                 .resolved()
                                                 .entries());
        assertEquals(expected, result);
    }

    @Test
    final void entries_resolved_distinct() {
        final String expected = TextIO.read(getClass(), "FileIndexTest.stream_distinct.txt");
        final String result = collected(FileIndex.of(testPath())
                                                 .resolved()
                                                 .distinct()
                                                 .entries());
        assertEquals(expected, result);
    }

    private String collected(final Stream<FileEntry> entrys) {
        final Path normal = testPath().toAbsolutePath().normalize();
        return entrys.map(FileEntry::path)
                     .map(normal::relativize)
                     .map(Path::toString)
                     .collect(Collectors.joining(NEW_LINE));
    }
}
