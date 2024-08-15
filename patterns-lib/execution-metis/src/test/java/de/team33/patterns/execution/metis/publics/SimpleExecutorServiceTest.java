package de.team33.patterns.execution.metis.publics;

import de.team33.patterns.execution.metis.SimpleExecutorService;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SimpleExecutorServiceTest {

    private final SimpleExecutorService executor = new SimpleExecutorService();

    private static void spendSomeTimeBeingBusy() {
        //final long time0 = System.currentTimeMillis();
        final AtomicInteger counter = new AtomicInteger(0);
        while (25_000_000 > counter.getAndIncrement()) {
            // nothing else to do
        }
        //System.out.append("time running (ms): ").println(System.currentTimeMillis() - time0);
    }

    @Test
    final void shutdown() throws InterruptedException {
        final AtomicInteger counter = new AtomicInteger(0);
        executor.execute(counter::incrementAndGet); // Should have an effect
        executor.shutdown();
        executor.execute(counter::incrementAndGet); // Should NOT have an effect
        assertTrue(executor.awaitTermination(10, TimeUnit.SECONDS));
        assertEquals(1, counter.get());
    }

    @Test
    final void shutdownNow() throws InterruptedException {
        final AtomicInteger counter = new AtomicInteger(0);
        IntStream.range(0, 1000)
                 .forEach(ignored -> executor.execute(counter::incrementAndGet)); // each should have an effect
        assertEquals(Collections.emptyList(), executor.shutdownNow());
        executor.execute(counter::incrementAndGet); // Should NOT have an effect
        assertTrue(executor.awaitTermination(10, TimeUnit.SECONDS));
        assertEquals(1000, counter.get());
    }

    @Test
    final void isShutdown() {
        assertFalse(executor.isShutdown());
        executor.shutdown();
        assertTrue(executor.isShutdown());
    }

    @Test
    final void isTerminated() throws InterruptedException {
        //noinspection Convert2MethodRef
        IntStream.range(0, 100)
                 .forEach(ignored -> executor.execute(() -> spendSomeTimeBeingBusy()));
        assertFalse(executor.isTerminated());
        executor.shutdown();
        // the threads should be busy enough that the executor doesn't finish immediately after the shutdown...
        assertFalse(executor.isTerminated());
        // the wait timout should definitely be long enough...
        assertTrue(executor.awaitTermination(10, TimeUnit.SECONDS));
        // now finally ...
        assertTrue(executor.isTerminated());
    }
}
