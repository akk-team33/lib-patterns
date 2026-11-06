package de.team33.patterns.collection.mneme.publics;

import de.team33.patterns.collection.mneme.ImmutableMap;
import de.team33.patterns.collection.mneme.ImmutableSet;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ImmutableMapTest {

    private final UUID uuid = UUID.randomUUID();
    private final Map<String, Integer> anyMap = new HashMap<>() {{
        put("zero", 0);
        put("one", 1);
        put("two", 2);
        put(uuid.toString(), uuid.hashCode());
    }};
    private final Map<String, Integer> anyOtherMap = new HashMap<>() {{
        put("ten", 10);
        put("eleven", 11);
        put("twelve", 12);
        put(uuid.toString(), uuid.hashCode());
    }};

    @Test
    final void entrySet() {
        final ImmutableSet<?> entries = ImmutableMap.proxy(anyMap).entrySet();
        assertEquals(anyMap.entrySet(), entries);
    }

    @Test
    final void keySet() {
        final ImmutableSet<?> keys = ImmutableMap.proxy(anyMap).keySet();
        assertEquals(anyMap.keySet(), keys);
    }

    @Test
    final void put() {
        final ImmutableMap<String, Integer> proxy = ImmutableMap.proxy(anyMap);
        assertThrows(UnsupportedOperationException.class, () -> proxy.put("three", 3));
        assertThrows(UnsupportedOperationException.class, () -> proxy.put("three", null));
        assertThrows(UnsupportedOperationException.class, () -> proxy.put(null, 3));
    }

    @Test
    final void putAll() {
        final ImmutableMap<String, Integer> proxy = ImmutableMap.proxy(anyMap);
        assertThrows(UnsupportedOperationException.class, () -> proxy.putAll(anyOtherMap));
    }

    @Test
    final void putIfAbsent() {
        final ImmutableMap<String, Integer> proxy = ImmutableMap.proxy(anyMap);
        assertThrows(UnsupportedOperationException.class, () -> proxy.putIfAbsent("two", -2));
        assertThrows(UnsupportedOperationException.class, () -> proxy.putIfAbsent("three", 3));
    }

    @Test
    final void merge() {
        final ImmutableMap<String, Integer> proxy = ImmutableMap.proxy(anyMap);
        assertThrows(UnsupportedOperationException.class, () -> proxy.merge("three", -3, null));
        assertThrows(UnsupportedOperationException.class, () -> proxy.merge("two", -2, (a, b) -> b));
        assertThrows(UnsupportedOperationException.class, () -> proxy.merge("three", -3, (a, b) -> b));
    }

    @Test
    final void replace() {
        final ImmutableMap<String, Integer> proxy = ImmutableMap.proxy(anyMap);
        assertThrows(UnsupportedOperationException.class, () -> proxy.replace("three", -3));
    }

    @Test
    final void testReplace() {
        final ImmutableMap<String, Integer> proxy = ImmutableMap.proxy(anyMap);
        assertThrows(UnsupportedOperationException.class, () -> proxy.replace("two", 2, -2));
    }

    @Test
    final void replaceAll() {
        final ImmutableMap<String, Integer> proxy = ImmutableMap.proxy(anyMap);
        assertThrows(UnsupportedOperationException.class, () -> proxy.replaceAll(null));
        assertThrows(UnsupportedOperationException.class, () -> proxy.replaceAll((a, b) -> -b));
    }

    @Test
    final void remove() {
        final ImmutableMap<String, Integer> proxy = ImmutableMap.proxy(anyMap);
        assertThrows(UnsupportedOperationException.class, () -> proxy.remove(null));
        assertThrows(UnsupportedOperationException.class, () -> proxy.remove("two"));
        assertThrows(UnsupportedOperationException.class, () -> proxy.remove("three"));
    }

    @Test
    final void testRemove() {
        final ImmutableMap<String, Integer> proxy = ImmutableMap.proxy(anyMap);
        assertThrows(UnsupportedOperationException.class, () -> proxy.remove(null, null));
        assertThrows(UnsupportedOperationException.class, () -> proxy.remove("two", -2));
        assertThrows(UnsupportedOperationException.class, () -> proxy.remove("three", -3));
    }

    @Test
    final void computeIfAbsent() {
        final ImmutableMap<String, Integer> proxy = ImmutableMap.proxy(anyMap);
        assertThrows(UnsupportedOperationException.class, () -> proxy.computeIfAbsent("three", k -> 3));
    }

    @Test
    final void computeIfPresent() {
        final ImmutableMap<String, Integer> proxy = ImmutableMap.proxy(anyMap);
        assertThrows(UnsupportedOperationException.class, () -> proxy.computeIfPresent("three", (a, b) -> -3));
    }

    @Test
    final void compute() {
        final ImmutableMap<String, Integer> proxy = ImmutableMap.proxy(anyMap);
        assertThrows(UnsupportedOperationException.class, () -> proxy.compute("three", (a, b) -> b - 3));
    }

    @Test
    final void clear() {
        final ImmutableMap<String, Integer> proxy = ImmutableMap.proxy(anyMap);
        assertThrows(UnsupportedOperationException.class, proxy::clear);
    }

    @Test
    final void values() {
        final List<Integer> values = ImmutableMap.proxy(anyMap).values().stream().toList();
        assertEquals(anyMap.values().stream().toList(), values);
    }

    @Test
    final void testEquals() {
        final ImmutableMap<String, Integer> immutable = ImmutableMap.proxy(anyMap);
        //noinspection EqualsWithItself
        assertEquals(immutable, immutable);
        assertEquals(anyMap, immutable);
        assertEquals(immutable, anyMap);
        assertNotEquals(immutable, anyOtherMap);
        //noinspection MisorderedAssertEqualsArguments
        assertNotEquals(immutable, this);
        assertNotEquals(this, immutable);
    }

    @Test
    final void testHashCode() {
        final ImmutableMap<String, Integer> immutable = ImmutableMap.proxy(anyMap);
        assertEquals(anyMap.hashCode(), immutable.hashCode());
    }

    @Test
    final void testToString() {
        final ImmutableMap<String, Integer> immutable = ImmutableMap.proxy(anyMap);
        assertEquals(anyMap.toString(), immutable.toString());
    }
}