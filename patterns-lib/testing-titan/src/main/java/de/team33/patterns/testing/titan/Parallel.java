package de.team33.patterns.testing.titan;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toList;

/**
 * @deprecated use de.team33.testing.async.thebe.Parallel instead -
 * see <a href="http://www.team33.de/dev/testing/1.x/testing-lib/async-thebe/">de.team33.testing:async-thebe</a>
 */
@Deprecated
public final class Parallel<R> {

    private final Report.Builder<R> report = new Report.Builder<>();
    private final AtomicInteger threadCounter = new AtomicInteger(0);
    private final AtomicInteger operationCounter = new AtomicInteger(0);
    private final List<Thread> threads;

    private Parallel(final int numberOfThreads, final Operation<R> operation) {
        this.threads = unmodifiableList(IntStream.range(0, numberOfThreads)
                                                 .mapToObj(threadIndex -> newThread(threadIndex, operation))
                                                 .collect(toList()));
    }

    /**
     * Returns a {@link Report} after executing a particular operation multiple times in parallel.
     * <p>
     * The operation is executed at least once by each of the designated threads and is repeated until all threads
     * have actually started.
     *
     * @param numberOfThreads The number of parallel threads in which the operation should be performed.
     * @param operation       The operation to be performed.
     * @param <R>             The type of result of the operation to be performed.
     */
    public static <R> Report<R> report(final int numberOfThreads, final Operation<R> operation) {
        return new Parallel<R>(numberOfThreads, operation).startThreads()
                                                          .joinThreads()
                                                          .report();
    }

    /**
     * Returns a {@link Stream} of results after executing a particular operation multiple times in parallel.
     * <p>
     * The operation is executed at least once by each of the designated threads and is repeated until all threads
     * have actually started.
     *
     * @param numberOfThreads The number of parallel threads in which the operation should be performed.
     * @param operation       The operation to be performed.
     * @param <R>             The type of result of the operation to be performed.
     * @throws Exception If any Exception occurs while executing the Operation
     */
    @SuppressWarnings("ProhibitedExceptionDeclared")
    public static <R> Stream<R> stream(final int numberOfThreads, final Operation<R> operation) throws Exception {
        return report(numberOfThreads, operation).reThrow(Error.class)
                                                 .reThrow(Exception.class)
                                                 .stream();
    }

    private Thread newThread(final int threadIndex, final Operation<R> operation) {
        //noinspection ObjectToString
        return new Thread(newRunnable(operation), this + ":" + threadIndex);
    }

    @SuppressWarnings({"BoundedWildcard", "OverlyBroadCatchBlock"})
    private Runnable newRunnable(final Operation<R> operation) {
        return () -> {
            final int threadIndex = threadCounter.getAndIncrement();
            for (int loop = 0; (loop == 0) || (threadCounter.get() < threads.size()); ++loop) {
                try {
                    report.add(operation.operate(new Context(threadIndex, operationCounter.getAndIncrement(), loop)));
                } catch (final Throwable e) {
                    report.add(e);
                }
            }
        };
    }

    private Parallel<R> startThreads() {
        for (final Thread thread : threads) {
            thread.start();
        }
        return this;
    }

    private Parallel<R> joinThreads() {
        for (final Thread thread : threads) {
            try {
                thread.join();
            } catch (final InterruptedException caught) {
                Thread.currentThread().interrupt();
                report.add(caught);
            }
        }
        return this;
    }

    private Report<R> report() {
        return report.build();
    }
}
