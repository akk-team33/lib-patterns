package de.team33.patterns.hierarchy.mab.publics;

import de.team33.patterns.hierarchy.mab.Nodes;
import de.team33.patterns.hierarchy.mab.testing.PathLister;
import de.team33.patterns.hierarchy.mab.testing.PathProblem;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Predicate;

import static java.util.function.Predicate.not;
import static org.junit.jupiter.api.Assertions.assertEquals;

class StreamerTest {

    private static final PathStreamer STREAMER = new PathStreamer(new PathLister(), null);

    @Test
    final void stream() {
        final List<String> expected = List.of("java", "de", "team33", "patterns", "hierarchy", "mab",
                                              "Nodes.java", "package-info.java");
        final Path entry = Path.of("src", "main", "java");

        final List<String> result = STREAMER.stream(entry)
                                            .map(Path::getFileName)
                                            .map(Path::toString)
                                            .toList();

        assertEquals(expected, result);
    }

    @Test
    final void skip_stream() {
        final List<String> expected = List.of("java", "de", "team33", "patterns", "hierarchy", "mab",
                                              "Nodes.java", "package-info.java");
        final Path node = Path.of("src");

        final PathStreamer streamer = STREAMER.skip(path -> path.endsWith("test"))
                                              .skip(path -> path.endsWith("resources"));
        final List<String> result = streamer.stream(node)
                                            .filter(not(path -> path.endsWith("src")))
                                            .filter(not(path -> path.endsWith("main")))
                                            .map(Path::getFileName)
                                            .map(Path::toString)
                                            .toList();

        assertEquals(expected, result);
    }

    @Test
    void skipHead_stream() {
        final Path path = Path.of("src");
        final PathStreamer streamer = STREAMER.skip(Files::isDirectory);

        final List<String> result = streamer.stream(path)
                                            .map(Path::getFileName)
                                            .map(Path::toString)
                                            .toList();

        assertEquals(List.of(), result);
    }

    static class PathStreamer extends Nodes.Streamer<Path, PathProblem, PathLister> {

        PathStreamer(final PathLister lister, final Predicate<Path> skipCondition) {
            super(lister, skipCondition);
        }

        PathStreamer skip(final Predicate<? super Path> condition) {
            return new PathStreamer(lister(), skipCondition().or(condition));
        }
    }
}