package de.team33.patterns.collection.mneme.publics;

import de.team33.patterns.collection.mneme.ImmutableSet;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ImmutableSetTest extends ImmutableCollectionTestBase<Object, ImmutableSet<Object>> {

    private static final List<Object> ANY_LIST = Arrays.asList(Instant.now(), null, 2, "3");
    private static final Set<Object> ANY_SET = new HashSet<>(ANY_LIST);

    ImmutableSetTest() {
        super(ImmutableSet.proxy(Set.of()), ImmutableSet.proxy(new HashSet<>(ANY_LIST)));
    }

    @Override
    final Object anyElement() {
        return this;
    }

    @Override
    final Collection<Object> anyElements() {
        //noinspection AssignmentOrReturnOfFieldWithMutableType
        return ANY_LIST;
    }

    @SuppressWarnings({"EqualsWithItself", "SimplifiableAssertion", "EqualsBetweenInconvertibleTypes"})
    @Test
    final void testEquals() {
        final ImmutableSet<Object> proxy = ImmutableSet.proxy(ANY_SET);
        assertTrue(proxy.equals(proxy));
        assertTrue(proxy.equals(ANY_SET));
        assertFalse(proxy.equals(ANY_LIST));
    }

    @Test
    final void testHashCode() {
        assertEquals(ANY_SET.hashCode(), ImmutableSet.proxy(ANY_SET).hashCode());
    }

    @Test
    final void proxy_iterator() {
        final Set<Object> result = new HashSet<>();
        ImmutableSet.proxy(ANY_SET).iterator().forEachRemaining(result::add);
        assertEquals(ANY_SET, result);
    }

    @Test
    final void proxy_size() {
        assertEquals(ANY_SET.size(), ImmutableSet.proxy(ANY_SET).size());
    }
}