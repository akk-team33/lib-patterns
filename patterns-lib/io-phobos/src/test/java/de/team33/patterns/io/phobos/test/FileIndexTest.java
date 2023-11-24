package de.team33.patterns.io.phobos.test;

import de.team33.patterns.io.phobos.FileEntry;
import de.team33.patterns.io.phobos.FileIndex;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class FileIndexTest {

    @Test
    void of() {
    }

    @Test
    void testOf() {
    }

    @Test
    void skipPath() {
    }

    @Test
    void skipEntry() {
    }

    @Test
    void stream() {
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "missing",
            "src/main/java"
    })
    final void stream(final String path) {
        final FileIndex index = FileIndex.of(path);
        final List<String> result = index.stream()
                                         .map(FileEntry::toString)
                                         .collect(Collectors.toList());
        assertEquals(path, result.get(0));
    }
}
