package de.team33.test.patterns.features.rhea;

import de.team33.patterns.features.rhea.FeatureHub;
import de.team33.patterns.features.rhea.FeatureSet;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.IntSupplier;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class FeatureSetTest {

    private static final AtomicInteger ATOMIC = new AtomicInteger();
    private static final Key<Random> RANDOM = test -> new Random();
    private static final Key<List<Integer>> INTEGERS = test -> new LinkedList<>();
    private static final Key<Void> VOID = test -> null;
    private static final Key<Integer> FIRST = test -> ATOMIC.getAndIncrement();
    private static final Key<IntSupplier> NEXT = test -> ATOMIC::getAndIncrement;

    private final FeatureHub<FeatureSetTest> features = new FeatureSet<>(this);

    @Test
    final void get() {
        assertInstanceOf(Random.class, features.get(RANDOM));
        assertSame(features.get(RANDOM), features.get(RANDOM));

        final List<Integer> expected = features.get(INTEGERS);
        IntStream.range(0, 100)
                 .forEach(index -> features.get(INTEGERS)
                                           .add(features.get(RANDOM).nextInt()));
        assertEquals(100, features.get(INTEGERS).size());
        assertSame(expected, features.get(INTEGERS));

        IntStream.range(0, 100)
                 .forEach(index -> assertEquals(0, features.get(FIRST)));
        IntStream.range(1, 100)
                 .forEach(index -> assertEquals(index, features.get(NEXT).getAsInt()));

        assertNull(features.get(VOID));
    }

    @FunctionalInterface
    private interface Key<R> extends Function<FeatureSetTest, R> {
    }
}
