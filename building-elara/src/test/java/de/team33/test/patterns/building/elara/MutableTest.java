package de.team33.test.patterns.building.elara;

import de.team33.patterns.building.elara.Mutable;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class MutableTest {

    private static final Random RANDOM = new Random();
    private static final List<Integer> VALUES = Stream.generate(() -> new BigInteger(RANDOM.nextInt(16), RANDOM))
                                                      .limit(4)
                                                      .map(BigInteger::intValue)
                                                      .collect(Collectors.toList());
    private static final Supplier<List<Integer>> NEW_SUBJECT = LinkedList::new;

    @Test
    final void singleUse() {
        final List<Integer> result = Mutable.builder(new LinkedList<Integer>())
                                            .setup(list -> list.addAll(VALUES))
                                            .subject();
        assertEquals(VALUES, result);
    }

    @Test
    final void multiUse() {
        final Mutable.Builder<List<Integer>, ?> builder = Mutable.builder(NEW_SUBJECT.get())
                                                                 .setup(list -> list.addAll(VALUES));
        assertEquals(VALUES, builder.subject());
        assertEquals(VALUES, builder.subject());
        assertEquals(VALUES, builder.subject());

        final List<Integer> expected = new ArrayList<>(VALUES);
        expected.addAll(VALUES);
        assertNotEquals(VALUES, expected);
        builder.setup(list -> list.addAll(VALUES));

        assertEquals(expected, builder.subject());
        assertEquals(expected, builder.subject());
        assertEquals(expected, builder.subject());
    }
}
