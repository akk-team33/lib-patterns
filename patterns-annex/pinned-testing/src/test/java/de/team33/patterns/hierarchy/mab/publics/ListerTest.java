package de.team33.patterns.hierarchy.mab.publics;

import de.team33.patterns.hierarchy.mab.testing.PathLister;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Deprecated
class ListerTest {

    private static final PathLister LISTER = new PathLister();

    @Test
    void list() {
        final List<String> expected = List.of("test");
        final Path path = Path.of("src");
        final List<String> result = LISTER.list(path)
                                          .stream()
                                          .map(Path::getFileName)
                                          .map(Path::toString)
                                          .toList();

        assertEquals(expected, result);
    }
}