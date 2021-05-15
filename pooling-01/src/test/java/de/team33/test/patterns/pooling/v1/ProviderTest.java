package de.team33.test.patterns.pooling.v1;

import de.team33.patterns.pooling.v1.Provider;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntSupplier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class ProviderTest {

    private static final int MAX = 16;

    private final AtomicInteger nextInt = new AtomicInteger(0);
    private final Provider<IntSupplier> provider = new Provider<>(this::newDispenser);

    private IntSupplier newDispenser() {
        final int result = nextInt.incrementAndGet();
        return () -> result;
    }

    @Test
    public final void run() throws InterruptedException {
        final Collection<Throwable> errors = new ConcurrentLinkedQueue<>();
        final Collection<Integer> results = new ConcurrentLinkedQueue<>();
        final Collection<Thread> threads = new ArrayList<>(MAX);
        for (int count = MAX; count > 0; count--) {
            threads.add(new Thread(() -> provider.run(e -> {
                try {
                    results.add(e.getAsInt());
                    Thread.sleep(10);
                } catch (final InterruptedException ex) {
                    errors.add(ex);
                    Thread.currentThread().interrupt();
                }
            })));
        }
        for (final Thread thread : threads) {
            thread.start();
        }
        for (final Thread thread : threads) {
            thread.join();
        }
        assertEquals(MAX, results.size());
        assertTrue(1 < results.stream().reduce(0, Math::max));
        assertEquals(0, errors.size());
    }

    @Test
    public final void runEx() throws InterruptedException {
        final Collection<Throwable> errors = new ConcurrentLinkedQueue<>();
        final Collection<Integer> results = new ConcurrentLinkedQueue<>();
        final Collection<Thread> threads = new ArrayList<>(MAX);
        for (int count = MAX; count > 0; count--) {
            threads.add(new Thread(() -> {
                try {
                    provider.runEx(e -> {
                        results.add(e.getAsInt());
                        Thread.sleep(10);
                    });
                } catch (final InterruptedException ex) {
                    errors.add(ex);
                    Thread.currentThread().interrupt();
                }
            }));
        }
        for (final Thread thread : threads) {
            thread.start();
        }
        for (final Thread thread : threads) {
            thread.join();
        }
        assertEquals(MAX, results.size());
        assertTrue(1 < results.stream().reduce(0, Math::max));
        assertEquals(0, errors.size());
    }

    @Test
    public final void get() {
        for (int i = 0; i < MAX; ++i) {
            assertEquals(Integer.valueOf(1), provider.get(IntSupplier::getAsInt));
        }
    }

    @Test
    public final void getEx() throws InterruptedException {
        for (int i = 0; i < MAX; ++i) {
            assertEquals(Integer.valueOf(1), provider.getEx(intSupplier -> {
                Thread.sleep(1);
                return intSupplier.getAsInt();
            }));
        }
    }
}
