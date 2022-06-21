package de.team33.test.patterns.refreshing.e1;

import de.team33.patterns.refreshing.e1.Fresh;
import de.team33.patterns.refreshing.e1.Fresh.Rule;
import de.team33.patterns.testing.e1.Parallel;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.Random;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

class FreshTest {

    private static final Logger LOG = Logger.getLogger(FreshTest.class.getCanonicalName());
    private static final int LIFETIME = 10;
    private static final Rule<Random> PLAIN_RULE = Fresh.rule(Random::new, LIFETIME);
    private static final Rule<Random> LOGGING_RULE = Fresh.rule(Random::new,
                                                                subject -> LOG.info(() -> "old: " + subject),
                                                                LIFETIME);

    private final Fresh<Random> freshRandom = new Fresh<>(PLAIN_RULE);

    @RepeatedTest(100)
    final void get_delivers() {
        assertInstanceOf(Random.class, freshRandom.get());
    }

    @Test
    final void get_new_if_timeout() throws Exception {
        final Fresh<Random> loggingFresh = new Fresh<>(LOGGING_RULE);
        final long time0 = System.currentTimeMillis();
        final Random first = loggingFresh.get();
        Parallel.apply(100, i -> {
            for (Random next = first; first == next; next = loggingFresh.get()) {
                // LOG.info("same");
            }
            return System.currentTimeMillis() - time0;
        })
                .reThrow(Error.class)
                .reThrow(Exception.class)
                .getResults()
                .forEach(delta -> assertTrue(delta > LIFETIME, () -> "delta = " + delta));
    }
}
