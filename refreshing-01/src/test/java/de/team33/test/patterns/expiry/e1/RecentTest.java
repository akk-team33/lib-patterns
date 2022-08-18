package de.team33.test.patterns.expiry.e1;

import de.team33.patterns.expiry.e1.Recent;
import de.team33.patterns.testing.e1.Parallel;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RecentTest {

    private static final long LIFETIME = 100; // milliseconds!
    private static final long MAX_LIFESPAN = LIFETIME + 30;

    private final Recent<Obsolescing> subject = new Recent<>(Obsolescing::new, LIFETIME);

    @Test
    final void obsolencing() throws Exception {
        final Obsolescing sample = new Obsolescing();
        Thread.sleep(MAX_LIFESPAN);
        assertThrows(IllegalStateException.class, () -> sample.getCreated());
    }

    @Test
    final void simple() throws Exception {
        // given ...
        final long time0 = System.currentTimeMillis();
        final Obsolescing first = subject.get();

        // when ...
        long counter = 0;
        while (first == subject.get()) {
            counter += 1;
        }
        final long delta = System.currentTimeMillis() - time0;

        // then ...
        assertTrue(delta > LIFETIME, () -> "delta = " + delta);
        final long finalCounter = counter;
        assertTrue(finalCounter > 0, () -> "counter = " + finalCounter);
        assertTrue(finalCounter > 100000, () -> "counter = " + finalCounter); // may fail on a slow system
    }

    @Test
    final void parallel() throws Exception {
        final long time0 = System.currentTimeMillis();
        final Obsolescing first = subject.get();

        final List<Long> results = Parallel.apply(10, index -> {
            long counter = 0;
            while (first == subject.get()) {
                counter += 1;
                Thread.sleep(0); // ... to give another thread a chance
            }
            return counter;
        }).reThrowAny().getResults();

        final long delta = System.currentTimeMillis() - time0;
        assertTrue(delta > LIFETIME, () -> "delta = " + delta);
        results.forEach(counter -> {
            assertTrue(counter > 0, () -> "counter = " + counter);
            assertTrue(counter > 500, () -> "counter = " + counter); // may fail on a slow system
        });
    }

    static class Obsolescing {

        private final Instant created;

        Obsolescing() {
            created = Instant.now();
        }

        final Instant getCreated() {
            return ifAlive(() -> created);
        }

        private <R> R ifAlive(final Supplier<R> supplier) {
            final Instant now = Instant.now();
            if (created.plusMillis(MAX_LIFESPAN).isAfter(now)) {
                return supplier.get();
            }
            throw new IllegalStateException("Instance is outdated - created = " + created + ", now = " + now);
        }
    }
}
