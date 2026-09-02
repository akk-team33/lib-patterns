package de.team33.patterns.collection.mneme.publics;

import de.team33.patterns.arbitrary.mimas.Generator;
import de.team33.patterns.collection.mneme.FinalEntry;
import de.team33.patterns.collection.mneme.FinalMap;
import de.team33.patterns.streamable.naiad.Streamable;
import de.team33.patterns.streamable.naiad.Streamer;
import org.junit.jupiter.api.Test;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.Temporal;
import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class FinalMapTest {

    private static final Generator GENERATOR = Generator.of(new SecureRandom());

    @Test
    final void empty() {
        assertEquals(Map.of(), FinalMap.empty());
    }

    @Test
    final void of_key_value() {
        final Map<Integer, String> origin = Map.of(GENERATOR.anyInt(), GENERATOR.anyString());
        final List<Map.Entry<Integer, String>> expected = origin.entrySet().stream().toList();
        final FinalMap<Integer, String> result = FinalMap.of(expected.get(0).getKey(),
                                                             expected.get(0).getValue());
        assertEquals(expected, result.entrySet().stream().toList());
    }

    @Test
    final void of_streamable_single() {
        final Map<Integer, String> origin = Map.of(GENERATOR.anyInt(), GENERATOR.anyString());
        final List<Map.Entry<Integer, String>> expected = origin.entrySet().stream().toList();
        final FinalMap<Integer, String> result = Streamer.of(expected.get(0))
                                                         .map(FinalMap::of);
        assertEquals(expected, result.entrySet().stream().toList());
    }

    @Test
    final void of_streamable_two() {
        final Map<Integer, String> origin = new HashMap<>() {{
            Stream.generate(GENERATOR::anyInt)
                  .distinct()
                  .limit(2)
                  .forEach(i -> put(i, GENERATOR.anyString()));
        }};
        final List<Map.Entry<Integer, String>> expected = origin.entrySet().stream().toList();
        final FinalMap<Integer, String> result = Streamer.of(expected.get(0))
                                                         .add(expected.get(1))
                                                         .map(FinalMap::of);
        assertEquals(expected, result.entrySet().stream().toList());
    }

    @Test
    final void of_streamable_three() {
        final Map<Integer, String> origin = new TreeMap<>() {{
            Stream.generate(GENERATOR::anyInt)
                  .distinct()
                  .limit(3)
                  .forEach(i -> put(i, GENERATOR.anyString()));
        }};
        final List<Map.Entry<Integer, String>> expected = origin.entrySet().stream().toList();
        final FinalMap<Integer, String> result = Streamer.of(expected.get(0))
                                                         .add(expected.get(1))
                                                         .add(expected.get(2))
                                                         .map(FinalMap::of);
        assertEquals(expected, result.entrySet().stream().toList());
    }

    @Test
    final void of_streamable_more() {
        final Map<Integer, String> origin = new LinkedHashMap<>() {{
            Stream.generate(GENERATOR::anyInt)
                  .distinct()
                  .limit(5)
                  .forEach(i -> put(i, GENERATOR.anyString()));
        }};
        final List<Map.Entry<Integer, String>> expected = origin.entrySet().stream().toList();
        final FinalMap<Integer, String> result = Streamer.of(expected.get(0))
                                                         .add(expected.get(1))
                                                         .add(expected.get(2))
                                                         .add(expected.get(3))
                                                         .add(expected.get(4))
                                                         .map(FinalMap::of);
        assertEquals(expected, result.entrySet().stream().toList());
    }

    @Test
    final void of_map() {
        final Map<Integer, String> origin = new TreeMap<>() {{
            Stream.generate(GENERATOR::anyInt)
                  .limit(GENERATOR.anyInt(200))
                  .forEach(i -> put(i, GENERATOR.anyString()));
        }};
        final List<Map.Entry<Integer, String>> expected = origin.entrySet().stream().toList();
        final FinalMap<Integer, String> result = FinalMap.of(origin);
        assertEquals(expected, result.entrySet().stream().toList());
    }

    @Test
    final void of_streamable_map_key_value() {
        final List<Instant> origin = Stream.generate(() -> Instant.now().plusMillis(GENERATOR.anyShort()))
                                           .distinct()
                                           .limit(100)
                                           .toList();
        final Map<Long, String> stage = new LinkedHashMap<>() {{
            origin.forEach(instant -> put(instant.toEpochMilli(), instant.toString()));
        }};
        final List<Map.Entry<Long, String>> expected = stage.entrySet().stream().toList();
        final FinalMap<Number, CharSequence> result = FinalMap.of(origin::stream,
                                                                  Instant::toEpochMilli,
                                                                  Instant::toString);
        assertEquals(expected, result.entrySet().stream().toList());
    }

    @Test
    final void of_streamable_map_entry() {
        final Streamable<Instant> origin = Stream.generate(() -> Instant.now().plusMillis(GENERATOR.anyInt()))
                                                 .distinct()
                                                 .limit(100)
                                                 .toList()::stream;
        final Map<Long, Instant> stage = new LinkedHashMap<>() {{
            origin.forEach(instant -> put(instant.toEpochMilli(), instant));
        }};
        final List<Map.Entry<Long, Instant>> expected = stage.entrySet().stream().toList();
        final FinalMap<Number, Temporal> result = FinalMap.of(origin, FinalEntry.mapping(Instant::toEpochMilli));
        assertEquals(expected, result.entrySet().stream().toList());
    }

    @Test
    final void of_nullable_distinct() {
        final List<Integer> origin = Arrays.asList(1, 2, 3, null, 2, 3, 4, null, 3, 4, 5);
        final Map<Integer, String> stage = new LinkedHashMap<>() {{
            origin.forEach(integer -> put(integer, (integer == null) ? null : integer.toString()));
        }};
        final var expected = stage.entrySet().stream().toList();
        final FinalMap<Number, CharSequence> result = FinalMap.of(stage);
        assertEquals(6, result.size());
        assertEquals(expected, result.entrySet().stream().toList());
        assertTrue(result.containsKey(null));
        assertTrue(result.containsValue(null));
    }

    @Test
    final void immutable() {
        final FinalMap<Integer, String> map = FinalMap.of(1, "one");

        assertThrows(UnsupportedOperationException.class,
                     () -> map.put(2, "two"));
        assertThrows(UnsupportedOperationException.class,
                     () -> map.remove(1));
        assertThrows(UnsupportedOperationException.class,
                     () -> map.entrySet().iterator().next().setValue("ONE"));
    }
}