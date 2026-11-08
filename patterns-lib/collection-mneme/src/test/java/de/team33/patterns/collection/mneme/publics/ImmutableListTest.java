package de.team33.patterns.collection.mneme.publics;

import de.team33.patterns.collection.mneme.FinalList;
import de.team33.patterns.collection.mneme.ImmutableList;
import de.team33.patterns.collection.mneme.testing.Supply;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class ImmutableListTest extends ImmutableCollectionTestBase<Object, ImmutableList<Object>> {

    private static final Supply SUPPLY = new Supply();

    private final List<Object> anyList = anyList();

    ImmutableListTest() {
        super(ImmutableList.proxy(new ArrayList<>()),
              ImmutableList.proxy(anyList()));
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

    @Test
    final void add_index() {
        assertThrows(UnsupportedOperationException.class, () -> empty.add(0, null));
        assertThrows(UnsupportedOperationException.class, () -> empty.add(0, this));
        assertThrows(UnsupportedOperationException.class, () -> stuff.add(0, null));
        assertThrows(UnsupportedOperationException.class, () -> stuff.add(0, this));
    }

    @Test
    final void addAll_index() {
        assertThrows(UnsupportedOperationException.class, () -> empty.addAll(2, anyList));
        assertThrows(UnsupportedOperationException.class, () -> empty.addAll(2, List.of()));
        assertThrows(UnsupportedOperationException.class, () -> empty.addAll(2, null));
        assertThrows(UnsupportedOperationException.class, () -> stuff.addAll(2, anyList));
        assertThrows(UnsupportedOperationException.class, () -> stuff.addAll(2, List.of()));
        assertThrows(UnsupportedOperationException.class, () -> stuff.addAll(2, null));
    }

    @Test
    final void set() {
        assertThrows(UnsupportedOperationException.class, () -> FinalList.empty().set(0, null));
        assertThrows(UnsupportedOperationException.class, () -> FinalList.empty().set(0, this));
    }

    @Test
    final void replaceAll() {
        final UnaryOperator<Object> identity = UnaryOperator.identity();
        assertThrows(UnsupportedOperationException.class, () -> FinalList.of(new Object()).replaceAll(null));
        assertThrows(UnsupportedOperationException.class, () -> FinalList.of(new Object()).replaceAll(identity));
    }

    @Test
    final void indexOf() {
        final ImmutableList<Object> proxy = ImmutableList.proxy(anyList);
        for (final Object element : anyList) {
            assertEquals(anyList.indexOf(element), proxy.indexOf(element));
        }
    }

    @Test
    final void lastIndexOf() {
        final ImmutableList<Object> proxy = ImmutableList.proxy(anyList);
        for (final Object element : anyList) {
            assertEquals(anyList.lastIndexOf(element), proxy.lastIndexOf(element));
        }
    }

    @Test
    final void subList() {
        final ImmutableList<Object> proxy = ImmutableList.proxy(anyList);
        assertEquals(anyList.subList(1, 3), proxy.subList(1, 3));
    }

    @SuppressWarnings({"SimplifiableAssertion", "EqualsWithItself"})
    @Test
    final void testEquals() {
        final ImmutableList<Object> proxy = ImmutableList.proxy(anyList);
        assertTrue(proxy.equals(proxy));
        assertTrue(anyList.equals(proxy));
        assertTrue(proxy.equals(anyList));
        assertFalse(proxy.equals(List.of()));
    }

    @Test
    final void testHashCode() {
        assertEquals(anyList.hashCode(), ImmutableList.proxy(anyList).hashCode());
    }
}