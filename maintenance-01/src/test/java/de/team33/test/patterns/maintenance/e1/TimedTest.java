package de.team33.test.patterns.maintenance.e1;

import de.team33.patterns.maintenance.e1.Timed;
import de.team33.patterns.testing.e1.Parallel;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class TimedTest {

    private static final int LIFETIME = 10;

    private final Timed<Random> random = new Timed<>(LIFETIME, Random::new);

    @RepeatedTest(100)
    final void get_delivers() {
        assertInstanceOf(Random.class, random.get());
    }

    @Test
    final void get_new_if_timeout() throws Exception {
        final long time0 = System.currentTimeMillis();
        final Random first = random.get();
        Parallel.apply(100, i -> {
            Random prev = first;
            Random next = random.get();
            while (prev == next) {
                prev = next;
                next = random.get();
            }
            return System.currentTimeMillis() - time0;
        })
                .reThrow(Error.class)
                .reThrow(RuntimeException.class)
                .reThrow(Exception.class)
                .getResults()
                .forEach(delta -> assertTrue(delta > LIFETIME, () -> "delta = " + delta));
    }
}
