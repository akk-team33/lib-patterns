package de.team33.test.patterns.building.elara;

import de.team33.patterns.building.elara.Mutable;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MutableTest {

    private static final List<Integer> VALUES = Arrays.asList(5, 7, 29);
    private static final Supplier<Set<Integer>> NEW_SUBJECT = TreeSet::new;

    @Test
    final void bySupplier() {
        final Set<Integer> result = Mutable.builder(NEW_SUBJECT)
                                           .setup(set -> set.add(VALUES.get(0)))
                                           .setup(set -> set.add(VALUES.get(1)))
                                           .setup(set -> set.add(VALUES.get(2)))
                                           .build();
        assertEquals(new HashSet<>(VALUES), result);
    }

    @Test
    final void bySubject() {
        final Set<Integer> result = Mutable.builder(new TreeSet<Integer>())
                                           .setup(set -> set.add(VALUES.get(0)))
                                           .setup(set -> set.add(VALUES.get(1)))
                                           .setup(set -> set.add(VALUES.get(2)))
                                           .build();
        assertEquals(new HashSet<>(VALUES), result);
    }
}
