package de.team33.patterns.expiry.tethys.next.publics;

import de.team33.patterns.expiry.tethys.Recent;
import de.team33.patterns.lazy.narvi.Lazy;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertTrue;

class RecentTest {

    private static final System.Logger LOG = System.getLogger(RecentTest.class.getCanonicalName());
    private static final AtomicInteger ATOMIC_INTEGER = new AtomicInteger(0);

    private final Meter meter = new Meter();
    private final Supplier<Long> initial = meter::next;
    private final long maxIdle = 3;
    private final long maxLiving = 20;
    private final Recent<Long> recent = new Recent<>(initial, maxIdle, maxLiving);

    @Test
    final void test_maxLiving() throws InterruptedException {
        final List<Long> result = collect(recent, 100, 2);
        LOG.log(System.Logger.Level.INFO, result);
        for (int i = 1; i < result.size(); ++i) {
            final long delta = result.get(i) - result.get(i - 1);
            assertTrue(delta >= maxLiving);
        }
    }

    @Test
    final void test_maxIdle() throws InterruptedException {
        final List<Long> result = collect(recent, 100, 4);
        LOG.log(System.Logger.Level.INFO, result);
        for (int i = 1; i < result.size(); ++i) {
            final long delta = result.get(i) - result.get(i - 1);
            assertTrue(delta >= maxIdle);
        }
    }

    private static <T extends Comparable<T>>
    List<T> collect(final Recent<T> recent, final int count, final long time) throws InterruptedException {
        final Set<T> result = new TreeSet<>();
        for (int index = 0; index < count; ++index) {
            result.add(recent.get());
            Thread.sleep(time);
        }
        return List.copyOf(result);
    }

    private static class Meter {

        private final Lazy<Long> time0 = Lazy.init(System::currentTimeMillis);

        final long next() {
            return System.currentTimeMillis() - time0.get();
        }
    }
}