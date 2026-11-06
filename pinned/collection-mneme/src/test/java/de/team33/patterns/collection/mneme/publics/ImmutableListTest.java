package de.team33.patterns.collection.mneme.publics;

import de.team33.patterns.collection.mneme.FinalList;
import de.team33.patterns.collection.mneme.ImmutableList;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.UnaryOperator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ImmutableListTest extends ImmutableCollectionTestBase<Object, ImmutableList<Object>> {

    private static final List<Object> ANY_LIST = Arrays.asList(Instant.now(), null, 2, "3");

    ImmutableListTest() {
        super(ImmutableList.proxy(new ArrayList<>()),
              ImmutableList.proxy(new ArrayList<>(ANY_LIST)));
    }

    @Override
    final Object anyElement() {
        return Instant.now();
    }

    @Override
    final Collection<Object> anyElements() {
        //noinspection AssignmentOrReturnOfFieldWithMutableType
        return ANY_LIST;
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
        assertThrows(UnsupportedOperationException.class, () -> empty.addAll(2, ANY_LIST));
        assertThrows(UnsupportedOperationException.class, () -> empty.addAll(2, List.of()));
        assertThrows(UnsupportedOperationException.class, () -> empty.addAll(2, null));
        assertThrows(UnsupportedOperationException.class, () -> stuff.addAll(2, ANY_LIST));
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
        final ImmutableList<Object> proxy = ImmutableList.proxy(ANY_LIST);
        for (final Object element : ANY_LIST) {
            assertEquals(ANY_LIST.indexOf(element), proxy.indexOf(element));
        }
    }

    @Test
    final void lastIndexOf() {
        final ImmutableList<Object> proxy = ImmutableList.proxy(ANY_LIST);
        for (final Object element : ANY_LIST) {
            assertEquals(ANY_LIST.lastIndexOf(element), proxy.lastIndexOf(element));
        }
    }

    @Test
    final void subList() {
        final ImmutableList<Object> proxy = ImmutableList.proxy(ANY_LIST);
        assertEquals(ANY_LIST.subList(1, 3), proxy.subList(1, 3));
    }
}