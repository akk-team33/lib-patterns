package de.team33.patterns.collection.mneme.publics;

import de.team33.patterns.collection.mneme.FinalList;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;
import java.util.function.UnaryOperator;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ImmutableListTest {

    @Test
    final void add() {
        assertThrows(UnsupportedOperationException.class, () -> FinalList.empty().add(null));
        assertThrows(UnsupportedOperationException.class, () -> FinalList.empty().add(this));
        assertThrows(UnsupportedOperationException.class, () -> FinalList.empty().add(0, null));
        assertThrows(UnsupportedOperationException.class, () -> FinalList.empty().add(0, this));
    }

    @Test
    final void set() {
        assertThrows(UnsupportedOperationException.class, () -> FinalList.empty().set(0, null));
        assertThrows(UnsupportedOperationException.class, () -> FinalList.empty().set(0, this));
    }

    @Test
    final void remove() {
        assertThrows(UnsupportedOperationException.class, () -> FinalList.empty().remove(null));
        assertThrows(UnsupportedOperationException.class, () -> FinalList.empty().remove(this));
        assertThrows(UnsupportedOperationException.class, () -> FinalList.of(this, null).remove(this));
        assertThrows(UnsupportedOperationException.class, () -> FinalList.of(this, null).remove(null));
        assertThrows(UnsupportedOperationException.class, () -> FinalList.of(this, null).remove(0));
        assertThrows(UnsupportedOperationException.class, () -> FinalList.of(this, null).remove(3));
        assertThrows(UnsupportedOperationException.class, () -> FinalList.of(this, null).remove(-2));
    }

    @Test
    final void clear() {
        assertThrows(UnsupportedOperationException.class, () -> FinalList.of(this).clear());
    }

    @Test
    final void addAll() {
        assertThrows(UnsupportedOperationException.class, () -> FinalList.empty().addAll(null));
        assertThrows(UnsupportedOperationException.class, () -> FinalList.empty().addAll(List.of()));
        assertThrows(UnsupportedOperationException.class, () -> FinalList.empty().addAll(List.of(1, 2, 3)));
        assertThrows(UnsupportedOperationException.class, () -> FinalList.of(this, null).addAll(1, List.of()));
        assertThrows(UnsupportedOperationException.class, () -> FinalList.of(this, null).addAll(1, List.of(this)));
    }

    @Test
    final void removeRange() {
        assertThrows(UnsupportedOperationException.class, () -> FinalList.empty().removeRange(1, 3));
        assertThrows(UnsupportedOperationException.class, () -> FinalList.of(0, 1, 2, 3).removeRange(0, 2));
    }

    @Test
    final void removeAll() {
        final Collection<Integer> empty = List.of();
        assertThrows(UnsupportedOperationException.class, () -> FinalList.of(0, 1, 2, 3).removeAll(null));
        assertThrows(UnsupportedOperationException.class, () -> FinalList.of(0, 1, 2, 3).removeAll(empty));
        assertThrows(UnsupportedOperationException.class, () -> FinalList.of(0, 1, 2, 3).removeAll(List.of(1, 3)));
    }

    @Test
    final void retainAll() {
        final Collection<Integer> empty = List.of();
        assertThrows(UnsupportedOperationException.class, () -> FinalList.of(0, 1, 2, 3).retainAll(null));
        assertThrows(UnsupportedOperationException.class, () -> FinalList.of(0, 1, 2, 3).retainAll(empty));
        assertThrows(UnsupportedOperationException.class, () -> FinalList.of(0, 1, 2, 3).retainAll(List.of(1, 3)));
        assertThrows(UnsupportedOperationException.class, () -> FinalList.empty().retainAll(empty));
    }

    @Test
    final void replaceAll() {
        final UnaryOperator<Object> identity = UnaryOperator.identity();
        assertThrows(UnsupportedOperationException.class, () -> FinalList.of(new Object()).replaceAll(null));
        assertThrows(UnsupportedOperationException.class, () -> FinalList.of(new Object()).replaceAll(identity));
    }

    @Test
    final void removeIf() {
        //noinspection DataFlowIssue
        assertThrows(UnsupportedOperationException.class, () -> FinalList.of(0, 1, 2, 3).removeIf(null));
        assertThrows(UnsupportedOperationException.class, () -> FinalList.of(0, 1, 2, 3).removeIf(any -> false));
        assertThrows(UnsupportedOperationException.class, () -> FinalList.of(0, 1, 2, 3).removeIf(any -> true));
    }
}