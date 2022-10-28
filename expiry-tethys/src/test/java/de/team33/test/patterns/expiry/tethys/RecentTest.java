package de.team33.test.patterns.expiry.tethys;

import de.team33.patterns.expiry.e1.Recent;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RecentTest {

    private static final long LIFETIME = 100; // milliseconds!
    private static final long MAX_LIFESPAN = LIFETIME + 30;

    private final Recent<Obsolescing> subject = new Recent<>(Obsolescing::new, LIFETIME);

    private static void sleep(final long millis) {
        try {
            Thread.sleep(millis);
        } catch (final InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException(e);
        }
    }

    @Test
    final void sequential() {
        final int limit = 10;
        final Instant time00 = Instant.now();
        final List<Long> results = IntStream.range(0, limit)
                                            .mapToLong(index -> {
                                                final Obsolescing first = subject.get();
                                                final Instant time0 = first.getCreated();
                                                assertEquals(index, first.getIndex());

                                                Obsolescing next = subject.get();
                                                while (next == first) {
                                                    next = subject.get();
                                                }
                                                return next.getCreated().toEpochMilli() - time0.toEpochMilli();
                                            })
                                            .boxed()
                                            .collect(Collectors.toList());
        final long delta = Instant.now().toEpochMilli() - time00.toEpochMilli();
        final long minExpected = limit * LIFETIME;
        assertTrue(delta >= minExpected, () -> String.format(" <delta> is expected to be geater than or equal to" +
                                                             " <minExpected> (%d) - but was %d", minExpected, delta));
        results.forEach(result -> assertTrue(result >= LIFETIME,
                                             () -> String.format(" <delta> is expected to be geater than or equal to" +
                                                                 " <LIFETIME> (%d) - but was %d",
                                                                 LIFETIME, result)));
    }

    @Test
    final void parallel() {
        final int limit = 100;
        final Instant time00 = Instant.now();
        final List<Long> results =
                IntStream.range(0, limit)
                         .parallel()
                         .mapToLong(index -> {
                             final Obsolescing first = subject.get();
                             final Instant time0 = first.getCreated();
                             if (index != 0) {
                                 assertNotEquals(index, first.getIndex());
                             }
                             Obsolescing next = subject.get();
                             while (next == first) {
                                 next = subject.get();
                             }
                             return next.getCreated().toEpochMilli() - time0.toEpochMilli();
                         })
                         .boxed()
                         .collect(Collectors.toList());
        final long delta = Instant.now().toEpochMilli() - time00.toEpochMilli();
        final long maxExpected = limit * LIFETIME;
        assertTrue(delta < maxExpected, () -> String.format(" <delta> is expected to be geater than or equal to" +
                                                            " <maxExpected> (%d) - but was %d", maxExpected, delta));
        results.forEach(result -> assertTrue(result >= LIFETIME,
                                             () -> String.format(" <delta> is expected to be geater than or equal to" +
                                                                 " <LIFETIME> (%d) - but was %d",
                                                                 LIFETIME, result)));
    }

    static class Obsolescing {

        private static final AtomicInteger NEXT_INDEX = new AtomicInteger(0);

        private final Instant created;
        private final int index;

        Obsolescing() {
            created = Instant.now();
            index = NEXT_INDEX.getAndIncrement();
        }

        final Instant getCreated() {
            return ifAlive(() -> created);
        }

        final int getIndex() {
            return ifAlive(() -> index);
        }

        private <R> R ifAlive(final Supplier<R> supplier) {
            final Instant now = Instant.now();
            if (created.plusMillis(MAX_LIFESPAN).compareTo(now) > 0) {
                return supplier.get();
            }
            throw new IllegalStateException("Instance is outdated - created = " + created + ", now = " + now);
        }
    }
}
