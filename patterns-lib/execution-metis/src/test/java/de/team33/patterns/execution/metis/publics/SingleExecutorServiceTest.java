package de.team33.patterns.execution.metis.publics;

import de.team33.patterns.execution.metis.SingleExecutorService;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class SingleExecutorServiceTest {

    private final SingleExecutorService subject = new SingleExecutorService();
    private final AtomicInteger counter = new AtomicInteger(0);

    @Test
    void execute() {
        subject.execute(newJob(5));
    }

    private Runnable newJob(final int depth) {
        if (0 < depth) {
            return () -> {
                counter.incrementAndGet();
                subject.execute(newJob(depth - 1));
            };
        } else {
            return () -> {};
        }
    }
}