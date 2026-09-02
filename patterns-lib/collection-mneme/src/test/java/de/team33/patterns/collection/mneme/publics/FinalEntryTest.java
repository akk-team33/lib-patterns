package de.team33.patterns.collection.mneme.publics;

import de.team33.patterns.arbitrary.mimas.Generator;
import de.team33.patterns.collection.mneme.FinalEntry;
import org.junit.jupiter.api.Test;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.AbstractMap;
import java.util.Map;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

class FinalEntryTest {

    private static final Generator GENERATOR = Generator.of(new SecureRandom());

    @Test
    final void of() {
        final Map.Entry<Integer, String> expected = new AbstractMap.SimpleEntry<>(GENERATOR.anyInt(),
                                                                                  GENERATOR.anyString());
        final FinalEntry<Number, CharSequence> result = FinalEntry.of(expected);
        assertEquals(expected, result);
        assertNotSame(expected, result);

        final FinalEntry<Object, Object> second = FinalEntry.of(result);
        assertSame(result, second);
    }

    @Test
    final void mapping_KeyValue() {
        final Instant origin = Instant.now().plusMillis(GENERATOR.anyShort());
        final FinalEntry<Long, String> expected = FinalEntry.of(origin.toEpochMilli(), origin.toString());
        final Function<Instant, Map.Entry<Long, String>> mapping =
                FinalEntry.mapping(Instant::toEpochMilli, Instant::toString);
        assertEquals(expected, mapping.apply(origin));
    }

    @Test
    final void mapping_Key() {
        final Instant origin = Instant.now().plusMillis(GENERATOR.anyShort());
        final FinalEntry<Long, Instant> expected = FinalEntry.of(origin.toEpochMilli(), origin);
        final Function<Instant, Map.Entry<Long, Instant>> mapping =
                FinalEntry.mapping(Instant::toEpochMilli);
        assertEquals(expected, mapping.apply(origin));
    }
}