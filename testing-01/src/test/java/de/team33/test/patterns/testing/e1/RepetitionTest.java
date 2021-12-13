package de.team33.test.patterns.testing.e1;

import de.team33.patterns.exceptional.e1.XConsumer;
import de.team33.patterns.exceptional.e1.XRunnable;
import de.team33.patterns.testing.e1.Repetition;
import de.team33.patterns.testing.e1.Report;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class RepetitionTest {

    private static final int PRIME_23 = 23;
    private static final int PRIME_101 = 101;

    @Test
    public final void simpleSuccess() {
        final List<Integer> indices = Collections.synchronizedList(new LinkedList<>());
        Repetition.run(PRIME_101, PRIME_23, ctx -> {
            indices.add(ctx.invocationIndex);
        });

        assertEquals(PRIME_101, indices.size(),
                     "In total, " + PRIME_101 + " invocation indices must have been collected.");
        final List<Integer> sorted = indices.stream().sorted().distinct().collect(toList());
        assertEquals(sorted.size(), indices.size(), "Each invocation index must be unique.");
        assertNotEquals(sorted, indices, "The invocations should NOT have taken place in their original order");
    }

    @Test
    public final void runParallel() throws Exception {
        final int limit = 100;
        final List<Integer> values = Collections.synchronizedList(new ArrayList<>(limit));
        final XConsumer<Integer, ?> method = index -> {
            Thread.sleep(2);
            values.add(index);
        };

        final Report report = Parallel.run(limit, method)
                                      .reThrow(Error.class)
                                      .reThrow(RuntimeException.class)
                                      .reThrow(Exception.class);

        assertEquals(Collections.emptyList(), report.getCaught(), "No exception is expected to be thrown");
        assertEquals(limit, values.size(), "The size of the collected <values> is expected to reach the <limit>");
        final List<Integer> sorted = values.stream().sorted().collect(toList());
        assertNotEquals(sorted, values, "The collected <values> are expected to be unordered");
        assertEquals(new HashSet<>(values).size(), values.size(), "The collected <values> are expected to be unique");
    }

    @Test
    public final void runParallel_withCaught() throws Exception {
        final int limit = 100;
        final List<Integer> values = Collections.synchronizedList(new ArrayList<>(limit));
        final XConsumer<Integer, ?> method = index -> {
            if (index % 13 == 0)
                throw new IllegalStateException("index = " + index);
            Thread.sleep(2);
            values.add(index);
        };

        final Report report = Parallel.run(limit, method)
                                      .reThrow(Error.class)
                                      .reThrow(Exception.class);

        assertEquals(limit / 13, report.getCaught(IllegalStateException.class).size());
        assertEquals(limit, values.size(), "The size of the collected <values> is expected to reach the <limit>");
        final List<Integer> sorted = values.stream().sorted().collect(toList());
        assertNotEquals(sorted, values, "The collected <values> are expected to be unordered");
        assertEquals(new HashSet<>(values).size(), values.size(), "The collected <values> are expected to be unique");
    }

    public static class Parallel {

        private final List<Thread> threads;
        private final Report.Builder report = Report.builder();

        public Parallel(final int count, final XConsumer<Integer, ?> method) {
            this.threads = IntStream.range(0, count)
                                    .mapToObj(index -> new Thread(() -> {
                                        try {
                                            method.accept(index);
                                        } catch (final Throwable e) {
                                            report.add(e);
                                        }
                                    }, this + ":" + index))
                                    .collect(toList());
        }

        public static Report run(final int count, final XConsumer<Integer, ?> method) throws InterruptedException {
            return new Parallel(count, method).startThreads()
                                              .joinThreads()
                                              .report();
        }

        private Parallel joinThreads() throws InterruptedException {
            for (Thread thread : threads) {
                thread.join();
            }
            return this;
        }

        private Parallel startThreads() {
            for (Thread thread : threads) {
                thread.start();
            }
            return this;
        }

        private Report report() {
            return report.build();
        }
    }

}
