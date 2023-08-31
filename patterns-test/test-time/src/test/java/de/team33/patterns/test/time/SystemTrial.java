package de.team33.patterns.test.time;

import org.junit.jupiter.api.RepeatedTest;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SystemTrial {

    private final AtomicInteger counter = new AtomicInteger(0);
    private final long time0 = System.currentTimeMillis();
    private final long nano0 = System.nanoTime();

    @RepeatedTest(100)
    final void currentTimeMillis() throws InterruptedException {
        Thread.sleep(1);
        final int index = counter.incrementAndGet();
        final long time = System.currentTimeMillis();
        assertEquals(1.0 * (index + time0), 1.0 * time, 1.0);
//        final long nano = System.nanoTime();
//        assertEquals(nano0, nano);
    }
}
