package de.team33.patterns.collection.mneme.publics;

import de.team33.patterns.arbitrary.mimas.Generator;
import de.team33.patterns.collection.mneme.ImmutableIterator;
import org.junit.jupiter.api.Test;

import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ImmutableIteratorTest {

    private static final Generator GENERATOR = Generator.of(new SecureRandom());

    private final Set<String> set = Stream.generate(GENERATOR::anyString)
                                          .distinct()
                                          .limit(GENERATOR.anyInt(5))
                                          .collect(Collectors.toCollection(HashSet::new));

    @Test
    final void remove_head() {
        final ImmutableIterator<String> iterator = ImmutableIterator.proxy(set.iterator());
        assertThrows(UnsupportedOperationException.class, iterator::remove);
    }

    @Test
    final void remove_next() {
        final ImmutableIterator<String> iterator = ImmutableIterator.proxy(set.iterator());
        if (iterator.hasNext()) {
            iterator.next();
            assertThrows(UnsupportedOperationException.class, iterator::remove);
        }
    }
}