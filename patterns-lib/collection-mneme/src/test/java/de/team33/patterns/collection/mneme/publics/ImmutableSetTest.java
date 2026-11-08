package de.team33.patterns.collection.mneme.publics;

import de.team33.patterns.collection.mneme.ImmutableSet;
import de.team33.patterns.collection.mneme.testing.Supply;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class ImmutableSetTest extends ImmutableCollectionTestBase<Object, ImmutableSet<Object>> {

    private static final Supply SUPPLY = new Supply();

    private final List<Object> anyList = anyList();
    private final Set<Object> anySet = new HashSet<>(anyList);

    ImmutableSetTest() {
        super(ImmutableSet.proxy(Set.of()), ImmutableSet.proxy(new HashSet<>(anyList())));
    }

    static List<Object> anyList() {
        return SUPPLY.stream(Supply::anyObject)
                     .limit(SUPPLY.anyInt(4, 8))
                     .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    final Object anyElement() {
        return SUPPLY.anyObject();
    }

    @Override
    final Collection<Object> anyElements() {
        return anyList();
    }

    @SuppressWarnings({"EqualsWithItself", "SimplifiableAssertion", "EqualsBetweenInconvertibleTypes"})
    @Test
    final void testEquals() {
        final ImmutableSet<Object> proxy = ImmutableSet.proxy(anySet);
        assertTrue(proxy.equals(proxy));
        assertTrue(anySet.equals(proxy));
        assertTrue(proxy.equals(anySet));
        assertFalse(proxy.equals(anyList));
    }

    @Test
    final void testHashCode() {
        assertEquals(anySet.hashCode(), ImmutableSet.proxy(anySet).hashCode());
    }

    @Test
    final void proxy_iterator() {
        final Set<Object> result = new HashSet<>();
        ImmutableSet.proxy(anySet).iterator().forEachRemaining(result::add);
        assertEquals(anySet, result);
    }

    @Test
    final void proxy_size() {
        assertEquals(anySet.size(), ImmutableSet.proxy(anySet).size());
    }
}