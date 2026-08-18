package de.team33.patterns.lazy.lambda.publics;

import de.team33.patterns.lazy.lambda.Features;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class FeaturesTest {

    private final String item1 = UUID.randomUUID().toString();
    private final String item2 = UUID.randomUUID().toString();
    private final String item3 = UUID.randomUUID().toString();
    private final Features features = new Features();

    private List<Object> toList() {
        return features.get(Key.TO_LIST, () -> List.of(item1, item2, item3));
    }

    @Test
    final void get() {
        assertNull(features.peek(Key.TO_LIST).orElse(null));
        final List<Object> result = toList();
        assertEquals(List.of(item1, item2, item3), result);
    }

    @Test
    final void peek() {
        assertNull(features.peek(Key.TO_LIST).orElse(null));
        assertNull(features.peek(Key.TO_STRING).orElse(null));
        final String expected = features.get(Key.TO_STRING, () -> toList().toString());
        final String result = features.peek(Key.TO_STRING).orElse(null);
        assertNotNull(features.peek(Key.TO_LIST).orElse(null));
        assertNotNull(features.peek(Key.TO_STRING).orElse(null));
        assertEquals(expected, result);
    }

    @Test
    final void reset() {
        peek();
        features.reset();
        assertNull(features.peek(Key.TO_LIST).orElse(null));
        assertNull(features.peek(Key.TO_STRING).orElse(null));
    }

    private interface Key<R> extends Features.Key<R> {

        Key<List<Object>> TO_LIST = named("TO_LIST");
        Key<String> TO_STRING = named("TO_STRING");

        // Not necessary but maybe useful ...
        // ----------------------------------
        static <R> Key<R> named(final String name) {
            return new Key<R>() {
                @Override
                public final String toString() {
                    return name;
                }
            };
        }
    }
}