package de.team33.test.patterns.expiry.tethys;

import de.team33.patterns.expiry.tethys.Recent;
import de.team33.patterns.testing.e1.Parallel;
import de.team33.patterns.tuple.janus.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RecentTest {

    private static final long IDLETIME = 10; // milliseconds!
    private static final long LIFETIME = 100; // milliseconds!

    private Recent<Obsolescing> recent;

    @BeforeEach
    final void beforeEach() {
        recent = new Recent<>(Obsolescing::new, IDLETIME, LIFETIME);
    }

    private static void sleep(final long millis) {
        try {
            Thread.sleep(millis);
        } catch (final InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException(e);
        }
    }

    @Test
    final void get_IDLETIME() {
        final Obsolescing first = recent.get();
        sleep(IDLETIME + 1);
        final Obsolescing second = recent.get();
        assertNotSame(first, second, "after <IDLETIME> it is not expected to get the same instance twice");
    }

    @Test
    final void get_LIFETIME() {
        final Obsolescing first = recent.get();
        final Instant created = first.getCreated();
        Obsolescing second = first;
        //noinspection ObjectEquality
        while (second == first) {
            sleep(1); // significantly less than IDLETIME
            second = recent.get();
        }
        final long delta = second.getCreated().toEpochMilli() - created.toEpochMilli();
        assertTrue(delta > LIFETIME,
                   () -> format("<delta> is expected to be greater than <LIFETIME> (%d) - but was %d",
                                LIFETIME, delta));
    }

    @Test
    final void get_parallel() throws Exception {
        // there must always be enough threads to be executed while others are sleeping ...
        final int limit = 1000;
        final Instant time00 = Instant.now();
        final List<Result> results = Parallel.apply(limit, index -> {
                                                 final Obsolescing first = recent.get();
                                                 final Instant created = first.getCreated();
                                                 Obsolescing second = first;
                                                 //noinspection ObjectEquality
                                                 while (second == first) {
                                                     sleep(IDLETIME + 1); // sic!
                                                     second = recent.get();
                                                 }
                                                 final long delta =
                                                         second.getCreated().toEpochMilli() -
                                                                 created.toEpochMilli();
                                                 return new Result(index, delta);
                                             })
                                             .reThrowAny()
                                             .getResults();
        final long delta = Instant.now().toEpochMilli() - time00.toEpochMilli();
        final long maxExpected = limit * LIFETIME;
        assertTrue(delta < maxExpected, () -> format(" <delta> is expected to be less than" +
                                                             " <maxExpected> (%d) - but was %d", maxExpected, delta));
        final String unexpected = results.stream()
                                         .filter(result -> result.delta() < LIFETIME)
                                         .map(result -> format("[index: %02d] " +
                                                                       "<delta> is expected to be greater than or " +
                                                                       "equal to <LIFETIME> (%d) - but was %d",
                                                               result.index(), LIFETIME, result.delta()))
                                         .collect(joining(format("%n")));
        assertEquals("", unexpected);
    }

    @SuppressWarnings("ClassTooDeepInInheritanceTree")
    static class Result extends Pair<Integer, Long> {

        Result(final int index, final long delta) {
            super(index, delta);
        }

        final int index() {
            return left();
        }

        final long delta() {
            return right();
        }
    }

    static class Obsolescing {

        private final Instant created;

        Obsolescing() {
            created = Instant.now();
        }

        final Instant getCreated() {
            return created;
        }
    }
}
