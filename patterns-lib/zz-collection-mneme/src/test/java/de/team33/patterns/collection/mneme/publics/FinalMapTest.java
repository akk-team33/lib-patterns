package de.team33.patterns.collection.mneme.publics;

import de.team33.patterns.arbitrary.mimas.Generator;
import de.team33.patterns.collection.mneme.FinalMap;
import de.team33.patterns.streamable.naiad.Streamer;
import org.junit.jupiter.api.Test;

import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class FinalMapTest {

    private static final Generator GENERATOR = Generator.of(new SecureRandom());

    @Test
    final void empty() {
        assertTrue(FinalMap.empty().isEmpty());
    }

    @Test
    final void of_streamer_map_reduce() {
        final List<Integer> source = Stream.generate(() -> GENERATOR.anyInt(0, 10))
                                           .limit(20000)
                                           .toList();
        final Map<Integer, Integer> expected = new LinkedHashMap<>() {{
            source.forEach(i -> put(i, i));
        }};
        final FinalMap<Integer, Integer> result = source.stream()
                                                        .map(i -> new AbstractMap.SimpleEntry<>(i, i))
                                                        .map(Streamer::of)
                                                        .reduce(Streamer.empty(), Streamer::addAll)
                                                        .map(FinalMap::of);
        assertEquals(expected, result);
        assertEquals(expected.entrySet().stream().toList(), result.entrySet().stream().toList());
    }

    @Test
    final void of_streamer_collect() {
        final List<Integer> source = Stream.generate(() -> GENERATOR.anyInt(0, 10))
                                           .limit(20000)
                                           .toList();
        final Map<Integer, Integer> expected = new LinkedHashMap<>() {{
            source.forEach(i -> put(i, i));
        }};
        final FinalMap<Integer, Integer> result = source.stream()
                                                        .map(i -> new AbstractMap.SimpleEntry<>(i, i))
                                                        .collect(Streamer::<Map.Entry<Integer, Integer>>empty,
                                                                 Streamer::add,
                                                                 Streamer::addAll)
                                                        .map(FinalMap::of);
        assertEquals(expected, result);
        assertEquals(expected.entrySet().stream().toList(), result.entrySet().stream().toList());
    }

    @Test
    final void of_nullable() {
        final List<Integer> source = Arrays.asList(1, 2, 3, null, 2, 3, 4, null, 3, 4, 5);
        final FinalMap<Integer, Integer> result = source.stream()
                                                        .collect(FinalMap::<Integer, Integer>builder,
                                                                 (map, value) -> map.put(value, value),
                                                                 FinalMap.Builder::putAll)
                                                        .build();
        assertTrue(result.containsKey(null));
        assertTrue(result.containsValue(null));
    }

    @Test
    final void of() {
        final List<Integer> source = Stream.generate(() -> GENERATOR.anyInt(0, 10))
                                           .limit(20)
                                           .toList();
        final Set<Integer> expected = new LinkedHashSet<>(source);
        assert expected.size() <= 10 : "Expected max. 10 elements - but was %d".formatted(expected.size());

        final FinalMap<Number, Number> result = source.stream()
                                                      .collect(FinalMap::<Number, Number>builder,
                                                               (map, value) -> map.put(value, value),
                                                               FinalMap.Builder::putAll)
                                                      .build();
        assertEquals(List.copyOf(expected), List.copyOf(result.keySet()));
        assertEquals(List.copyOf(expected), List.copyOf(result.values()));
    }

    @Test
    final void of_map() {
        final Map<Integer, String> source = new LinkedHashMap<>();
        source.put(3, "three");
        source.put(1, "one");
        source.put(4, "four");

        final FinalMap<Integer, String> result = FinalMap.of(source);

        assertEquals(source, result);
        assertEquals(source.entrySet().stream().toList(),
                     result.entrySet().stream().toList());
    }

    @Test
    final void of_map_is_independent_of_source() {
        final Map<Integer, String> source = new LinkedHashMap<>();
        source.put(1, "one");
        source.put(2, "two");

        final FinalMap<Integer, String> result = FinalMap.of(source);

        source.put(1, "ONE");
        source.put(3, "three");

        assertEquals(Map.of(1, "one", 2, "two"), result);
    }

    @Test
    final void builder() {
        final FinalMap<Integer, String> result = FinalMap.<Integer, String>builder()
                                                         .put(1, "one")
                                                         .put(2, "two")
                                                         .put(1, "ONE")
                                                         .remove(2)
                                                         .put(3, "three")
                                                         .build();

        assertEquals(List.of(1, 3), result.keySet().stream().toList());
        assertEquals(List.of("ONE", "three"), result.values().stream().toList());
    }

    @Test
    final void builder_key_value() {
        final FinalMap.Builder<Integer, String> builder =
                FinalMap.builder(GENERATOR.anyInt(), GENERATOR.anyString());
        assertEquals(builder.build(), builder.build());
    }

    @Test
    final void builder_map() {
        final Map<Integer, String> source =
                new LinkedHashMap<>(Map.of(2, "two", 1, "one"));

        final FinalMap<Integer, String> result =
                FinalMap.builder(source).build();

        assertEquals(source.entrySet().stream().toList(),
                     result.entrySet().stream().toList());
    }

    @Test
    final void builder_putAll_map() {
        final Map<Integer, String> source = new LinkedHashMap<>();
        source.put(2, "two");
        source.put(1, "one");

        final FinalMap<Integer, String> result =
                FinalMap.<Integer, String>builder()
                        .put(0, "zero")
                        .putAll(source)
                        .build();

        assertEquals(List.of(0, 2, 1), result.keySet().stream().toList());
    }

    @Test
    final void builder_putAll_streamable() {
        final FinalMap<Integer, String> result =
                FinalMap.<Integer, String>builder()
                        .putAll(Streamer.of(
                                Map.entry(3, "three"),
                                Map.entry(1, "one"),
                                Map.entry(3, "THREE")))
                        .build();

        assertEquals(List.of(3, 1), result.keySet().stream().toList());
        assertEquals(List.of("THREE", "one"), result.values().stream().toList());
    }

    @Test
    final void builder_putAll_builder() {
        final FinalMap<Integer, String> source =
                FinalMap.<Integer, String>builder()
                        .put(2, "two")
                        .put(1, "one")
                        .build();

        final FinalMap<Integer, String> result =
                FinalMap.<Integer, String>builder()
                        .put(0, "zero")
                        .putAll(FinalMap.builder(source))
                        .build();

        assertEquals(List.of(0, 2, 1), result.keySet().stream().toList());
    }

    @Test
    final void builder_is_independent_of_build_result() {
        final FinalMap.Builder<Integer, String> builder =
                FinalMap.<Integer, String>builder()
                        .put(1, "one");

        final FinalMap<Integer, String> first = builder.build();

        builder.put(2, "two");

        assertEquals(List.of(1), first.keySet().stream().toList());
        assertEquals(List.of(1, 2), builder.build().keySet().stream().toList());
    }

    @Test
    final void build_does_not_modify_builder() {
        final FinalMap.Builder<Integer, String> builder =
                FinalMap.<Integer, String>builder()
                        .put(1, "one")
                        .put(2, "two");

        builder.build();

        assertEquals(2, builder.build().size());
    }

    @Test
    final void immutable() {
        final FinalMap<Integer, String> map =
                FinalMap.of(1, "one");

        assertThrows(UnsupportedOperationException.class,
                     () -> map.put(2, "two"));

        assertThrows(UnsupportedOperationException.class,
                     () -> map.remove(1));

        assertThrows(UnsupportedOperationException.class,
                     () -> map.entrySet().iterator().next().setValue("ONE"));
    }

    @Test
    final void duplicate_keys() {
        final FinalMap<Integer, String> result =
                FinalMap.<Integer, String>builder()
                        .put(1, "one")
                        .put(2, "two")
                        .put(1, "ONE")
                        .put(3, "three")
                        .put(2, "TWO")
                        .build();

        assertEquals(List.of(1, 2, 3), result.keySet().stream().toList());
        assertEquals(List.of("ONE", "TWO", "three"), result.values().stream().toList());
    }
}