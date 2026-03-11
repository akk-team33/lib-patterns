package de.team33.patterns.hierarchy.mab.publics;

import de.team33.patterns.hierarchy.mab.Hierarchy;
import de.team33.patterns.hierarchy.mab.testing.FileEntry;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.List;

import static java.util.function.Predicate.not;
import static org.junit.jupiter.api.Assertions.assertEquals;

class HierarchyTest {

    private static final Hierarchy.Streamer<FileEntry> STREAMER = Hierarchy.streamer();

    @Test
    final void streamer_stream() {
        final List<String> expected = List.of("java", "de", "team33", "patterns",
                                              "hierarchy", "mab", "Hierarchy.java", "package-info.java");
        final FileEntry entry = new FileEntry(Path.of("src", "main", "java"));

        final List<String> result = STREAMER.stream(entry)
                                            .map(FileEntry::path)
                                            .map(Path::getFileName)
                                            .map(Path::toString)
                                            .toList();

        assertEquals(expected, result);
    }

    @Test
    void streamer_skip_stream() {
        final List<String> expected = List.of("java", "de", "team33", "patterns",
                                              "hierarchy", "mab", "Hierarchy.java", "package-info.java");
        final FileEntry entry = new FileEntry(Path.of("src"));

        final Hierarchy.Streamer<FileEntry> streamer = STREAMER.skip(e -> e.path().endsWith("test"))
                                                               .skip(e -> e.path().endsWith("resources"));
        final List<String> result = streamer.stream(entry)
                                            .map(FileEntry::path)
                                            .filter(not(path -> path.endsWith("src")))
                                            .filter(not(path -> path.endsWith("main")))
                                            .map(Path::getFileName)
                                            .map(Path::toString)
                                            .toList();

        assertEquals(expected, result);
    }

    @Test
    void streamer_skipFirst_stream() {
        final FileEntry entry = new FileEntry(Path.of("src"));
        final Hierarchy.Streamer<FileEntry> streamer = STREAMER.skip(FileEntry::isDirectory);

        final List<String> result = streamer.stream(entry)
                                            .map(FileEntry::path)
                                            .map(Path::getFileName)
                                            .map(Path::toString)
                                            .toList();

        assertEquals(List.of(), result);
    }
}