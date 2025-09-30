package de.team33.patterns.stream.galatea.publics;

import de.team33.patterns.stream.galatea.Streamable;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StreamableTest {

    @Test
    final void stream() {
        final List<String> expected = List.of("a", "b", "c");
        final Streamable<String> streamable = expected::stream;
        final List<String> result = streamable.stream().toList();
        assertEquals(expected, result);
    }
}