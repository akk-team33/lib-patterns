package de.team33.test.patterns.expiry.tethys;

import de.team33.patterns.expiry.tethys.Recent;
import de.team33.patterns.tuple.janus.Trip;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RecentTest {

    private static final long IDLETIME = 30; // milliseconds!
    private static final long LIFETIME = 100; // milliseconds!
    private static final long MAX_LIFESPAN = LIFETIME + 30;

    private final Recent<Obsolescing> subject = new Recent<>(Obsolescing::new, IDLETIME, LIFETIME);

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
        final List<Result> results = IntStream.range(0, limit)
                                              .mapToObj(index -> {
                                                  final Obsolescing first = subject.get();
                                                  final Instant time0 = first.getCreated();
                                                  final int firstIndex = first.getIndex();

                                                  Obsolescing next = subject.get();
                                                  // noinspection ObjectEquality
                                                  while (next == first) {
                                                      next = subject.get();
                                                  }
                                                  final long delta = next.getCreated().toEpochMilli()
                                                                     - time0.toEpochMilli();
                                                  return new Result(index, firstIndex, delta);
                                              })
                                              .collect(Collectors.toList());
        final long delta = Instant.now().toEpochMilli() - time00.toEpochMilli();
        final long minExpected = limit * LIFETIME;
        assertTrue(delta >= minExpected,
                   () -> format("<delta> is expected to be geater than or equal to <minExpected> (%d) - but was %d",
                                minExpected, delta));
        final long matchingIndices = results.stream()
                                            .filter(result -> result.red().equals(result.green()))
                                            .count();
        assertEquals(limit, matchingIndices,
                     () -> format("<matchingIndices> is expected to be equal to <limit> (%d) - but was %d",
                                  limit, matchingIndices));
        results.forEach(result -> assertTrue(result.blue() >= LIFETIME,
                                             () -> format("[index: %d] " +
                                                          "<delta> is expected to be geater than or equal to " +
                                                          "<LIFETIME> (%d) - but was %d",
                                                          result.red(), LIFETIME, result.blue())));
    }

    @Test
    final void parallel() {
        final int limit = 20;
        final Instant time00 = Instant.now();
        final List<Result> results = IntStream.range(0, limit)
                                              .parallel()
                                              .mapToObj(index -> {
                                                  final Obsolescing first = subject.get();
                                                  final Instant time0 = first.getCreated();
                                                  final int firstIndex = first.getIndex();

                                                  Obsolescing next = subject.get();
                                                  while (next == first) {
                                                      next = subject.get();
                                                      sleep(0);
                                                  }
                                                  final long delta = next.getCreated().toEpochMilli()
                                                                     - time0.toEpochMilli();
                                                  return new Result(index, firstIndex, delta);
                                              })
                                              .collect(Collectors.toList());
        final long delta = Instant.now().toEpochMilli() - time00.toEpochMilli();
        final long maxExpected = limit * LIFETIME;
        assertTrue(delta < maxExpected, () -> format(" <delta> is expected to be less than" +
                                                     " <maxExpected> (%d) - but was %d", maxExpected, delta));
        final long matchingIndices = results.stream()
                                            .filter(result -> result.index() == result.instanceIndex())
                                            .count();
        assertTrue(matchingIndices < limit,
                   () -> format("<matchingIndices> is expected to be less than <limit> (%d) - but was %d",
                                limit, matchingIndices));
        results.forEach(result -> assertTrue(result.delta() >= LIFETIME,
                                             () -> format("[index: %d] " +
                                                          "<delta> is expected to be geater than or equal to " +
                                                          "<LIFETIME> (%d) - but was %d",
                                                          result.index(), LIFETIME, result.delta())));
    }

    static class Result extends Trip<Integer, Integer, Long> {

        Result(final int index, final int instanceIndex, final long delta) {
            super(index, instanceIndex, delta);
        }

        final int index() {
            return red();
        }

        final int instanceIndex() {
            return green();
        }

        final long delta() {
            return blue();
        }
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
            return created;
        }

        final int getIndex() {
            return index;
        }
    }
}
