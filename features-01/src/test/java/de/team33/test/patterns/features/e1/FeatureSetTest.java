package de.team33.test.patterns.features.e1;

import de.team33.patterns.features.e1.FeatureSet;
import de.team33.patterns.features.e1.Key;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntSupplier;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FeatureSetTest {

    private static final AtomicInteger ATOMIC = new AtomicInteger();
    private static final Key<Random> RANDOM = Random::new;
    private static final Key<List<Integer>> INTEGERS = LinkedList::new;
    private static final Key<Void> VOID = () -> null;
    private static final Key<Integer> FIRST = ATOMIC::getAndIncrement;
    private static final Key<IntSupplier> NEXT = () -> ATOMIC::getAndIncrement;

    private final FeatureSet features = new FeatureSet();

    @SuppressWarnings("ConstantConditions")
    @Test
    final void get() {
        assertTrue(features.get(RANDOM) instanceof Random);
        assertSame(features.get(RANDOM), features.get(RANDOM));

        final List<Integer> expected = features.get(INTEGERS);
        IntStream.range(0, 100).forEach(index -> features.get(INTEGERS).add(features.get(RANDOM).nextInt()));
        assertEquals(100, features.get(INTEGERS).size());
        assertSame(expected, features.get(INTEGERS));

        IntStream.range(0, 100).forEach(index -> assertEquals(0, features.get(FIRST)));
        IntStream.range(1, 100).forEach(index -> assertEquals(index, features.get(NEXT).getAsInt()));

        assertNull(features.get(VOID));
    }
}
