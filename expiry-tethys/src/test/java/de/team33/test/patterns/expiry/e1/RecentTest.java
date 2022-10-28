package de.team33.test.patterns.expiry.e1;

import com.sun.org.apache.xpath.internal.SourceTree;
import de.team33.patterns.expiry.e1.Recent;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    final void obsolencing() {
        final Obsolescing sample = new Obsolescing();
        sleep(MAX_LIFESPAN);
        assertThrows(IllegalStateException.class, sample::getCreated);
    }

    @Test
    final void instant() {
        final Instant start = Instant.now();
        final Instant result = Stream.generate(Instant::now)
                                     .limit(1000000)
                                     .reduce(start, (left, right) -> {
                                         if (left.compareTo(right) <= 0) {
                                             return right;
                                         } else {
                                             throw new IllegalStateException(
                                                     String.format("left > right ...%n" +
                                                                   "   left = %s%n" +
                                                                   "   right = %s%n", left, right));
                                         }
                                     });
        assertTrue(start.compareTo(result) < 0, () -> String.format("start = %s, result = %s", start, result));
        final Instant now = Instant.now();
        assertTrue(now.compareTo(result) >= 0, () -> String.format("now = %s, result = %s", now, result));
    }

    @Test
    final void sequential() {
        final int limit = 10;
        final Instant time00 = Instant.now();
        IntStream.range(0, limit)
                 .forEach(index -> {
                     final Instant time0 = Instant.now();
                     final Obsolescing first = subject.get();
                     assertTrue(first.getCreated().compareTo(time0) >= 0,
                                () -> String.format("[index = %d]" +
                                                    " <first.created> is expected to be greater or equal to %s -" +
                                                    " but was %s", index, time0, first.getCreated()));
                     assertEquals(index, first.getIndex());
                     while (first == subject.get()) {
                         first.getIndex();
                     }
                     final long delta = Instant.now().toEpochMilli() - time0.toEpochMilli();
                     assertTrue(delta >= LIFETIME,
                                () -> String.format("[index: %d]" +
                                                    " <delta> is expected to be geater than" +
                                                    " <LIFETIME> (%d) - but was %d",
                                                    index, LIFETIME, delta));
                 });
        final long delta = Instant.now().toEpochMilli() - time00.toEpochMilli();
        assertTrue(delta >= limit * LIFETIME, () -> "delta = " + delta);
    }

    @Test
    final void parallel() {
        // given ...
        final Instant time0 = Instant.now();
        final Obsolescing first = subject.get();

        // when ...
        final List<Long> result = IntStream.range(0, 100)
                                           .parallel()
                                           .mapToLong(index -> {
                                               long counter = 0;
                                               while (first == subject.get()) {
                                                   counter += 1;
                                                   sleep(0); // ... to give another thread a chance
                                               }
                                               return counter;
                                           })
                                           .boxed()
                                           .collect(Collectors.toList());
//        final List<Long> results = Parallel.apply(10, index -> {
//            long counter = 0;
//            while (first == subject.get()) {
//                counter += 1;
//                sleep(0); // ... to give another thread a chance
//            }
//            return counter;
//        }).reThrowAny().getResults();
        final long delta = Instant.now().toEpochMilli() - time0.toEpochMilli();

        // then ...
        assertTrue(delta > LIFETIME, () -> "delta = " + delta);
        result.forEach(counter -> {
            assertTrue(counter > 0, () -> "counter = " + counter); // may fail on a very slow system
            assertTrue(counter > 500, () -> "counter = " + counter); // may fail on a slow system
        });
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
