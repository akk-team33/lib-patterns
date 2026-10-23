package de.team33.patterns.collection.mneme.publics;

import de.team33.patterns.arbitrary.mimas.Generator;
import de.team33.patterns.collection.mneme.ImmutableListIterator;
import org.junit.jupiter.api.Test;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ImmutableListIteratorTest {

    private static final Generator GENERATOR = Generator.of(new SecureRandom());

    private final List<String> list = Stream.generate(GENERATOR::anyString)
                                            .limit(GENERATOR.anyInt(5))
                                            .collect(Collectors.toCollection(ArrayList::new));

    @Test
    final void remove_head() {
        final ImmutableListIterator<String> iterator = ImmutableListIterator.proxy(list.listIterator());
        assertThrows(UnsupportedOperationException.class, iterator::remove);
    }

    @Test
    final void remove_next() {
        final ImmutableListIterator<String> iterator = ImmutableListIterator.proxy(list.listIterator());
        if (iterator.hasNext()) {
            iterator.next();
            assertThrows(UnsupportedOperationException.class, iterator::remove);
        }
    }

    @Test
    final void remove_prev() {
        final ImmutableListIterator<String> iterator = ImmutableListIterator.proxy(list.listIterator(list.size()));
        if (iterator.hasPrevious()) {
            iterator.previous();
            assertThrows(UnsupportedOperationException.class, iterator::remove);
        }
    }

    @Test
    final void set_head() {
        final ImmutableListIterator<String> iterator = ImmutableListIterator.proxy(list.listIterator());
        assertThrows(UnsupportedOperationException.class, () -> iterator.set(GENERATOR.anyString()));
    }

    @Test
    final void set_next() {
        final ImmutableListIterator<String> iterator = ImmutableListIterator.proxy(list.listIterator());
        if (iterator.hasNext()) {
            iterator.next();
            assertThrows(UnsupportedOperationException.class, () -> iterator.set(GENERATOR.anyString()));
        }
    }

    @Test
    final void set_prev() {
        final ImmutableListIterator<String> iterator = ImmutableListIterator.proxy(list.listIterator(list.size()));
        if (iterator.hasPrevious()) {
            iterator.previous();
            assertThrows(UnsupportedOperationException.class, () -> iterator.set(GENERATOR.anyString()));
        }
    }

    @Test
    final void add_head() {
        final ImmutableListIterator<String> iterator = ImmutableListIterator.proxy(list.listIterator());
        assertThrows(UnsupportedOperationException.class, () -> iterator.add(GENERATOR.anyString()));
    }

    @Test
    final void add_tail() {
        final ImmutableListIterator<String> iterator = ImmutableListIterator.proxy(list.listIterator(list.size()));
        assertThrows(UnsupportedOperationException.class, () -> iterator.add(GENERATOR.anyString()));
    }

    @Test
    final void nextIndex() {
        assertEquals(list.size(), ImmutableListIterator.proxy(list.listIterator(list.size())).nextIndex());
    }

    @Test
    final void previousIndex() {
        assertEquals(list.size() - 1, ImmutableListIterator.proxy(list.listIterator(list.size())).previousIndex());
    }
}