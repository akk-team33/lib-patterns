package de.team33.test.patterns.refreshing.e1;

import de.team33.patterns.refreshing.e1.Fresh;
import de.team33.patterns.refreshing.e1.Fresh.Rule;
import de.team33.patterns.testing.e1.Parallel;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class FreshTest {

    private static final int LIFETIME = 10;
    private static final Rule<Random> RANDOM_RULE = new Rule<>(Random::new, LIFETIME);

    private final Fresh<Random> freshRandom = new Fresh<>(RANDOM_RULE);

    @RepeatedTest(100)
    final void get_delivers() {
        assertInstanceOf(Random.class, freshRandom.get());
    }

    @Test
    final void get_new_if_timeout() throws Exception {
        final long time0 = System.currentTimeMillis();
        final Random first = freshRandom.get();
        Parallel.apply(100, i -> {
            Random prev = first;
            Random next = freshRandom.get();
            while (prev == next) {
                prev = next;
                next = freshRandom.get();
            }
            return System.currentTimeMillis() - time0;
        })
                .reThrow(Error.class)
                .reThrow(Exception.class)
                .getResults()
                .forEach(delta -> assertTrue(delta > LIFETIME, () -> "delta = " + delta));
    }
}
