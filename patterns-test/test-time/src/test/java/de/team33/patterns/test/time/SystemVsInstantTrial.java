package de.team33.patterns.test.time;

import org.junit.jupiter.api.RepeatedTest;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SystemVsInstantTrial {

    @RepeatedTest(1000)
    final void currentTimeMillis() throws InterruptedException {
        Thread.sleep(0);
        final Instant now = Instant.now();
        final long expected = System.currentTimeMillis();
        assertEquals(expected, now.toEpochMilli());
    }
}
