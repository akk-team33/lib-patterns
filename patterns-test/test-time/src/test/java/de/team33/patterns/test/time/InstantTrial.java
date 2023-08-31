package de.team33.patterns.test.time;

import org.junit.jupiter.api.RepeatedTest;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InstantTrial {

    private final AtomicInteger counter = new AtomicInteger(0);
    private final long time0;
    private final int nano0;

    public InstantTrial() {
        final Instant now = Instant.now();
        time0 = now.toEpochMilli();
        nano0 = now.getNano();
    }

    @RepeatedTest(100)
    final void currentTimeMillis() throws InterruptedException {
        Thread.sleep(1);
        final int index = counter.incrementAndGet();
        final Instant now = Instant.now();
        final long time = now.toEpochMilli();
        assertEquals(1.0 * (time0 + index), 1.0 * time, 1.0);
//        final int nano = now.getNano();
//        assertEquals(nano0, nano);
    }
}
