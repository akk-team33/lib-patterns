package de.team33.patterns.collection.ceres.publics;

import de.team33.patterns.collection.ceres.Mapping;
import de.team33.patterns.collection.ceres.testing.Supply;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("ClassWithTooManyMethods")
class MappingTest extends Supply
{

    private Map<String, String> samples;

    @BeforeEach
    final void before() {
        samples = Collections.unmodifiableMap(anyMap(3));
    }

    @Test
    final void put() {
        final String key = anyString();
        final String value = anyString();
        final Map<String, String> expected = Collections.singletonMap(key, value);
        final HashMap<String, String> stage = new HashMap<>(1);

        final Map<String, String> result = Mapping.put(stage, key, value);

        assertEquals(expected, result);
        assertSame(stage, result);
    }

    @Test
    final void putAll() {
        final Map<String, String> stage = new HashMap<>(3);

        final Map<String, String> result = Mapping.putAll(stage, samples);

        assertEquals(samples, result);
        assertSame(stage, result);
    }

    @Test
    final void clear() {
        final Map<String, String> stage = new HashMap<>(samples);

        final Map<String, String> result = Mapping.clear(stage);

        assertEquals(Collections.emptyMap(), result);
        assertSame(stage, result);
    }

    @Test
    final void remove() {
        final String key = samples.keySet()
                                  .stream()
                                  .findAny()
                                  .orElseThrow(() -> new AssertionError("not found"));
        final Map<String, String> expected = new HashMap<>(samples);
        expected.remove(key);

        final Map<String, String> stage = new TreeMap<>(samples);
        final Map<String, String> result = Mapping.remove(stage, key);

        assertEquals(expected, result);
        assertSame(stage, result);
    }

    @Test
    final void remove_null() {
        final Map<String, String> stage = new TreeMap<>(samples);
        final Map<String, String> result = Mapping.remove(stage, null);

        assertEquals(samples, result);
        assertSame(stage, result);

        // cross-check ...

        assertThrows(NullPointerException.class, () -> stage.remove(null));
    }

    @Test
    final void remove_subject_null() {
        final String aString = anyString();
        final double aDouble = anyDouble();
        assertThrows(NullPointerException.class, () -> Mapping.remove(null, aString));
        assertThrows(NullPointerException.class, () -> Mapping.remove(null, aDouble));
        assertThrows(NullPointerException.class, () -> Mapping.remove(null, null));
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    @Test
    final void remove_incompatible() {
        final Map<String, String> stage = new TreeMap<>(samples);
        final Map<String, String> result = Mapping.remove(stage, anyDouble());

        assertEquals(samples, result);
        assertSame(stage, result);

        // cross-check ...
        final double aDouble = anyDouble();
        assertThrows(ClassCastException.class, () -> stage.remove(aDouble));
    }

    @Test
    final void containsKey() {
        samples.keySet().forEach(key -> assertTrue(Mapping.containsKey(samples, key)));
    }

    @Test
    final void containsKey_null() {
        final Map<String, String> subject = new TreeMap<>(samples);

        assertFalse(Mapping.containsKey(subject, null));

        // cross-check ...
        assertThrows(NullPointerException.class, () -> subject.containsKey(null));
    }

    @Test
    final void containsKey_subject_null() {
        final String aString = anyString();
        final long aLong = anyLong();
        assertThrows(NullPointerException.class, () -> Mapping.containsKey(null, aString));
        assertThrows(NullPointerException.class, () -> Mapping.containsKey(null, aLong));
        assertThrows(NullPointerException.class, () -> Mapping.containsKey(null, null));
    }

    @Test
    final void containsKey_incompatible() {
        final long longKey = anyLong();
        final Map<String, String> subject = new TreeMap<>(samples);

        assertFalse(Mapping.containsKey(subject, longKey));

        // cross-check ...
        // noinspection SuspiciousMethodCalls
        assertThrows(ClassCastException.class, () -> subject.containsKey(longKey));
    }

    @Test
    final void containsValue() {
        samples.values().forEach(value -> assertTrue(Mapping.containsValue(samples, value)));
    }

    @Test
    final void containsValue_null() {
        final Map<String, String> subject = new TreeMap<>(samples) {
            @Override
            public boolean containsValue(final Object value) {
                return super.containsValue(Objects.requireNonNull(value));
            }
        };

        assertFalse(Mapping.containsValue(subject, null));

        // cross-check ...
        assertThrows(NullPointerException.class, () -> subject.containsValue(null));
    }

    @Test
    final void containsValue_subject_null() {
        final String aString = anyString();
        final double aDouble = anyDouble();
        assertThrows(NullPointerException.class, () -> Mapping.containsValue(null, aString));
        assertThrows(NullPointerException.class, () -> Mapping.containsValue(null, aDouble));
        assertThrows(NullPointerException.class, () -> Mapping.containsValue(null, null));
    }

    @Test
    final void containsValue_incompatible() {
        final long longValue = anyLong();
        final Map<String, String> subject = new TreeMap<>(samples) {
            @Override
            public boolean containsValue(final Object value) {
                //noinspection RedundantCast
                return super.containsValue((String) value);
            }
        };

        assertFalse(Mapping.containsValue(subject, longValue));

        // cross-check ...
        // noinspection SuspiciousMethodCalls
        assertThrows(ClassCastException.class, () -> subject.containsValue(longValue));
    }

    @Test
    final void get() {
        samples.forEach((key, expected) -> {
            final String result = Mapping.get(samples, key);
            assertEquals(expected, result);
        });
    }

    @Test
    final void get_null() {
        final Map<String, String> subject = new TreeMap<>(samples);
        assertNull(Mapping.get(subject, null));

        // cross-check ...
        assertThrows(NullPointerException.class, () -> subject.get(null));
    }

    @Test
    final void get_subject_null() {
        final String aString = anyString();
        final float aFloat = anyFloat();
        assertThrows(NullPointerException.class, () -> Mapping.get(null, aString));
        assertThrows(NullPointerException.class, () -> Mapping.get(null, aFloat));
        assertThrows(NullPointerException.class, () -> Mapping.get(null, null));
    }

    @Test
    final void get_incompatible() {
        final long longKey = anyLong();
        final Map<String, String> subject = new TreeMap<>(samples);
        assertNull(Mapping.get(subject, longKey));

        // cross-check ...
        // noinspection SuspiciousMethodCalls
        assertThrows(ClassCastException.class, () -> subject.get(longKey));
    }

    @Test
    final void proxy() {
        final Map<String, String> origin = anyMap(4);
        final Map<String, String> copy = new TreeMap<>(origin);
        final Map<String, String> proxy = Mapping.proxy(origin);

        assertEquals(origin.size(), proxy.size());
        assertEquals(origin.toString(), proxy.toString());
        assertEquals(origin.hashCode(), proxy.hashCode());
        assertEquals(origin.equals(copy), proxy.equals(copy));
        assertEquals(copy.equals(origin), copy.equals(proxy));
    }
}
