package de.team33.patterns.collection.mneme.publics;

import de.team33.patterns.collection.mneme.ImmutableCollection;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

abstract class ImmutableCollectionTestBase<E, C extends ImmutableCollection<E>> {

    @SuppressWarnings("PackageVisibleField")
    final C empty;
    @SuppressWarnings("PackageVisibleField")
    final C stuff;

    @SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
    ImmutableCollectionTestBase(final C empty, final C stuff) {
        this.empty = empty;
        this.stuff = stuff;
    }

    abstract E anyElement();

    abstract Collection<E> anyElements();

    @Test
    final void add() {
        assertThrows(UnsupportedOperationException.class, () -> empty.add(null));
        assertThrows(UnsupportedOperationException.class, () -> empty.add(anyElement()));
        assertThrows(UnsupportedOperationException.class, () -> stuff.add(null));
        assertThrows(UnsupportedOperationException.class, () -> stuff.add(anyElement()));
    }

    @Test
    final void remove() {
        assertThrows(UnsupportedOperationException.class, () -> empty.remove(null));
        assertThrows(UnsupportedOperationException.class, () -> empty.remove(anyElement()));
        assertThrows(UnsupportedOperationException.class, () -> stuff.remove(anyElement()));
        assertThrows(UnsupportedOperationException.class, () -> stuff.remove(null));
    }

    @Test
    final void clear() {
        assertThrows(UnsupportedOperationException.class, empty::clear);
        assertThrows(UnsupportedOperationException.class, stuff::clear);
    }

    @Test
    final void addAll() {
        assertThrows(UnsupportedOperationException.class, () -> empty.addAll(null));
        assertThrows(UnsupportedOperationException.class, () -> empty.addAll(List.of()));
        assertThrows(UnsupportedOperationException.class, () -> empty.addAll(anyElements()));
        assertThrows(UnsupportedOperationException.class, () -> stuff.addAll(null));
        assertThrows(UnsupportedOperationException.class, () -> stuff.addAll(List.of()));
        assertThrows(UnsupportedOperationException.class, () -> stuff.addAll(anyElements()));
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    @Test
    final void removeAll() {
        final List<E> emptyList = List.of();
        assertThrows(UnsupportedOperationException.class, () -> empty.removeAll(null));
        assertThrows(UnsupportedOperationException.class, () -> empty.removeAll(emptyList));
        assertThrows(UnsupportedOperationException.class, () -> empty.removeAll(anyElements()));
        assertThrows(UnsupportedOperationException.class, () -> empty.removeAll(List.of(2, "3")));
        assertThrows(UnsupportedOperationException.class, () -> stuff.removeAll(null));
        assertThrows(UnsupportedOperationException.class, () -> stuff.removeAll(emptyList));
        assertThrows(UnsupportedOperationException.class, () -> stuff.removeAll(anyElements()));
        assertThrows(UnsupportedOperationException.class, () -> stuff.removeAll(List.of(2, "3")));
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    @Test
    final void retainAll() {
        final List<E> emptyList = List.of();
        assertThrows(UnsupportedOperationException.class, () -> empty.retainAll(null));
        assertThrows(UnsupportedOperationException.class, () -> empty.retainAll(emptyList));
        assertThrows(UnsupportedOperationException.class, () -> empty.retainAll(anyElements()));
        assertThrows(UnsupportedOperationException.class, () -> empty.retainAll(List.of(2, "3")));
        assertThrows(UnsupportedOperationException.class, () -> stuff.retainAll(null));
        assertThrows(UnsupportedOperationException.class, () -> stuff.retainAll(emptyList));
        assertThrows(UnsupportedOperationException.class, () -> stuff.retainAll(anyElements()));
        assertThrows(UnsupportedOperationException.class, () -> stuff.retainAll(List.of(2, "3")));
    }

    @Test
    final void removeIf() {
        assertThrows(UnsupportedOperationException.class, () -> empty.removeIf(null));
        assertThrows(UnsupportedOperationException.class, () -> empty.removeIf(any -> false));
        assertThrows(UnsupportedOperationException.class, () -> empty.removeIf(any -> true));
        assertThrows(UnsupportedOperationException.class, () -> stuff.removeIf(null));
        assertThrows(UnsupportedOperationException.class, () -> stuff.removeIf(any -> false));
        assertThrows(UnsupportedOperationException.class, () -> stuff.removeIf(any -> true));
    }
}