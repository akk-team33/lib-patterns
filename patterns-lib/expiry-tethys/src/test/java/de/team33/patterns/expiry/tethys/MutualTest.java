package de.team33.patterns.expiry.tethys;

import de.team33.patterns.lazy.narvi.Lazy;
import de.team33.testing.async.thebe.Parallel;
import de.team33.testing.bridging.styx.Bridger;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MutualTest extends Bridger {

    private static final System.Logger LOG = System.getLogger(MutualTest.class.getCanonicalName());
    private static final int MAX_IDLE = 10;
    private static final int MAX_LIVING = 50;

    private final Lazy<Long> time0 = Lazy.init(System::currentTimeMillis);
    private final Supplier<Long> initial = () -> System.currentTimeMillis() - time0.get();
    private final Mutual<Long, Exception> recent = new Mutual<>(initial::get,
                                                                Duration.ofMillis(MAX_IDLE),
                                                                Duration.ofMillis(MAX_LIVING));

    private <T extends Comparable<T>>
    List<T> collect(final Mutual<? extends T, ?> mutual, final int count, final int time) throws Exception {
        final Set<T> result = new TreeSet<>();
        for (int index = 0; index < count; ++index) {
            result.add(mutual.get());
            bridge(time);
        }
        return List.copyOf(result);
    }

    @Test
    final void get_maxLiving() throws Exception {
        final List<Long> result = collect(recent, 100, MAX_IDLE / 2);
        LOG.log(System.Logger.Level.INFO, result);
        for (int i = 1; i < result.size(); ++i) {
            final long delta = result.get(i) - result.get(i - 1);
            assertTrue(delta >= MAX_LIVING);
        }
    }

    @Test
    final void get_maxIdle() throws Exception {
        //noinspection MultiplyOrDivideByPowerOfTwo
        final List<Long> result = collect(recent, 25, MAX_IDLE * 2);
        LOG.log(System.Logger.Level.INFO, result);
        for (int index = 1; index < result.size(); ++index) {
            final long delta = result.get(index) - result.get(index - 1);
            assertTrue(delta >= MAX_IDLE);
        }
    }

    @Test
    final void get_parallel_within_IDLE_TIME() throws Exception {
        final AtomicInteger nextIndex = new AtomicInteger(0);
        final int maxIdle = 100;
        final int maxLiving = 1000;
        final Mutual<Sample, Exception> mutual = new Mutual<>(() -> new Sample(nextIndex.getAndIncrement()),
                                                              Duration.ofMillis(maxIdle),
                                                              Duration.ofMillis(maxLiving));
        final Sample first = mutual.get();
        final List<Sample> result = Parallel.stream(20, context -> {
                                                           Thread.sleep(0, 1000); // give other threads a chance
                                                           return mutual.get();
                                                       })
                                                       .distinct()
                                                       .toList();
        assertTrue(first.created.plusMillis(maxLiving).isAfter(Instant.now()), "result takes too much time!");
        assertEquals(List.of(first), result);
    }

    private record Sample(int index, Instant created) {

        private Sample(final int index) {
            this(index, Instant.now());
        }
    }
}
