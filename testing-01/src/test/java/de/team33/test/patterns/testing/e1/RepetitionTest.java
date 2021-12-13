package de.team33.test.patterns.testing.e1;

import de.team33.patterns.exceptional.e1.XConsumer;
import de.team33.patterns.exceptional.e1.XFunction;
import de.team33.patterns.testing.e1.Report;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class RepetitionTest {

    @Test
    final void runParallel() throws Throwable {
        final int limit = 100;
        final List<Integer> values = Collections.synchronizedList(new ArrayList<>(limit));
        final XConsumer<Integer, InterruptedException> method = index -> {
            Thread.sleep(2);
            values.add(index);
        };

        final Report report = Parallel.run(limit, method)
                                      .reThrow(Throwable.class);

        assertEquals(Collections.emptyList(), report.getCaught(), "No exception is expected to be thrown");
        assertEquals(limit, values.size(), "The size of the collected <values> is expected to reach the <limit>");
        final List<Integer> sorted = values.stream().sorted().collect(toList());
        assertNotEquals(sorted, values, "The collected <values> are expected to be unordered");
        assertEquals(new HashSet<>(values).size(), values.size(), "The collected <values> are expected to be unique");
    }

    @Test
    final void runParallel_withCaught() throws Exception {
        final int limit = 100;
        final List<Integer> values = Collections.synchronizedList(new ArrayList<>(limit));
        final XConsumer<Integer, PositiveException> method = index -> {
            if (index % 13 == 0) {
                throw new PositiveException("index = " + index);
            } else {
                values.add(index);
            }
        };
        final int expectedToFail = ((limit - 1) / 13) + 1;

        final Report report = Parallel.run(limit, method)
                                      .reThrow(Error.class)
                                      .reThrow(Exception.class, PositiveException.class, NegativeException.class);

        assertEquals(expectedToFail, report.getCaught(PositiveException.class).size());
        assertEquals(0, report.getCaught(NegativeException.class).size());
        assertEquals(limit - expectedToFail, values.size());
    }

    public static class Parallel {

        private final List<Thread> threads;
        private final Report.Builder report = Report.builder();

        private Parallel(final int count, final XConsumer<Integer, ?> method) {
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

        private <R> Thread newThread(final int index, final XFunction<Integer, R, ?> method) {
            return new Thread(() -> {
                try {
                    method.apply(index);
                } catch (final Throwable e) {
                    report.add(e);
                }
            }, this + ":" + index);
        }

        public static Report run(final int count, final XConsumer<Integer, ?> method) throws InterruptedException {
            return new Parallel(count, method).startThreads()
                                              .joinThreads()
                                              .report();
        }

        private Parallel startThreads() {
            for (final Thread thread : threads) {
                thread.start();
            }
            return this;
        }

        private Parallel joinThreads() throws InterruptedException {
            for (final Thread thread : threads) {
                thread.join();
            }
            return this;
        }

        private Report report() {
            return report.build();
        }
    }

    static class PositiveException extends Exception {

        PositiveException(final String message) {
            super(message);
        }
    }

    static class NegativeException extends Exception {
    }
}
