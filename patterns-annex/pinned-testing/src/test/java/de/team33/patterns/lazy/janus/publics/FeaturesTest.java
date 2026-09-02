package de.team33.patterns.lazy.janus.publics;

import de.team33.patterns.lazy.janus.Features;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class FeaturesTest {

    private final Features features = new Features();

    @Test
    final void get() {
        final List<Object> expected = toList();
        assertEquals(expected, List.of(item01(), item02(), item03()));
        assertSame(expected.get(0), item01());
        assertSame(expected.get(1), item02());
        assertSame(expected.get(2), item03());
        assertSame(expected, toList());
    }

    @Test
    final void reset() {
        final List<Object> unexpected = toList();
        features.reset();
        assertNotEquals(unexpected.get(0), item01());
        assertNotEquals(unexpected.get(1), item02());
        assertNotEquals(unexpected.get(2), item03());
        assertNotEquals(unexpected, toList());
    }

    private String item01() {
        return features.get(Key.ITEM_01, () -> UUID.randomUUID().toString());
    }

    private UUID item02() {
        return features.get(Key.ITEM_02, UUID::randomUUID);
    }

    private Instant item03() {
        return features.get(Key.ITEM_03, Instant::now);
    }

    private List<Object> toList() {
        return features.get(Key.TO_LIST, () -> List.of(item01(), item02(), item03()));
    }

    @Override
    public final String toString() {
        return features.get(Key.TO_STRING, () -> toList().toString());
    }

    private interface Key<R> extends Features.Key<R> {

        Key<String> ITEM_01 = named("ITEM_01");
        Key<UUID> ITEM_02 = named("ITEM_02");
        Key<Instant> ITEM_03 = named("ITEM_03");
        Key<List<Object>> TO_LIST = named("TO_LIST");
        Key<String> TO_STRING = named("TO_STRING");

        // Not necessary but maybe useful ...
        // ----------------------------------
        static <R> Key<R> named(final String name) {
            return new Key<>() {
                @Override
                public final String toString() {
                    return name;
                }
            };
        }
    }
}