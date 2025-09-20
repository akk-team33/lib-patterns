package de.team33.patterns.expiry.tethys.next.publics;

import de.team33.patterns.expiry.tethys.Recent;
import de.team33.patterns.lazy.narvi.Lazy;
import de.team33.testing.bridging.styx.Bridger;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertTrue;

class RecentTest extends Bridger {

    private static final System.Logger LOG = System.getLogger(RecentTest.class.getCanonicalName());
    private static final int MAX_IDLE = 5;
    private static final int MAX_LIVING = 20;

    private final Lazy<Long> time0 = Lazy.init(System::currentTimeMillis);
    private final Supplier<Long> initial = () -> System.currentTimeMillis() - time0.get();
    private final Recent<Long> recent = new Recent<>(initial, MAX_IDLE, MAX_LIVING);

    private <T extends Comparable<T>>
    List<T> collect(final Recent<T> recent, final int count, final int time) throws InterruptedException {
        final Set<T> result = new TreeSet<>();
        for (int index = 0; index < count; ++index) {
            result.add(recent.get());
            bridge(time);
        }
        return List.copyOf(result);
    }

    @Test
    final void get_maxLiving() throws InterruptedException {
        final List<Long> result = collect(recent, 100, MAX_IDLE / 2);
        LOG.log(System.Logger.Level.INFO, result);
        for (int i = 1; i < result.size(); ++i) {
            final long delta = result.get(i) - result.get(i - 1);
            assertTrue(delta >= MAX_LIVING);
        }
    }

    @Test
    final void get_maxIdle() throws InterruptedException {
        final List<Long> result = collect(recent, 100, MAX_IDLE * 2);
        LOG.log(System.Logger.Level.INFO, result);
        for (int index = 1; index < result.size(); ++index) {
            final long delta = result.get(index) - result.get(index - 1);
            assertTrue(delta >= MAX_IDLE);
        }
    }
}