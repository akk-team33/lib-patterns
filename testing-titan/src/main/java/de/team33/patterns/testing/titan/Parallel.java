package de.team33.patterns.testing.titan;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toList;

/**
 * A tool/utility to execute operations multiple times in parallel for test purposes.
 */
public final class Parallel<R> {

    private final int minNumberOfOperations;
    private final List<Thread> threads;
    private final Report.Builder<R> report = new Report.Builder<>();
    private final AtomicInteger operationCounter = new AtomicInteger(0);
    private final AtomicInteger executionCounter = new AtomicInteger(0);

    private Parallel(final int minNumberOfOperations, final int numberOfThreads, final Operation<R> operation) {
        this.minNumberOfOperations = minNumberOfOperations;
        this.threads = unmodifiableList(IntStream.range(0, numberOfThreads)
                                                 .mapToObj(threadIndex -> newThread(threadIndex, operation))
                                                 .collect(toList()));
    }

    /**
     * Returns a {@link Report} after executing a particular operation multiple times in parallel.
     *
     * @param minNumberOfOperations The minimum number of times the operation should be performed.
     * @param numberOfThreads       The number of parallel threads in which the operation should be performed.
     * @param operation             The operation to be performed.
     * @param <R>                   The type of result of the operation to be performed.
     * @see #report(int, Operation)
     */
    public static <R> Report<R> report(final int minNumberOfOperations,
                                       final int numberOfThreads,
                                       final Operation<R> operation) {
        return new Parallel<R>(minNumberOfOperations, numberOfThreads, operation).startThreads()
                                                                                 .joinThreads()
                                                                                 .report();
    }

    /**
     * Returns a {@link Report} after executing a particular operation multiple times in parallel.
     *
     * @param numberOfThreads The number of parallel threads in which the operation should be performed.
     * @param operation       The operation to be performed.
     * @param <R>             The type of result of the operation to be performed.
     * @see #report(int, int, Operation)
     */
    public static <R> Report<R> report(final int numberOfThreads, final Operation<R> operation) {
        return report(0, numberOfThreads, operation);
    }

    /**
     * Returns a {@link Stream} of results after executing a particular operation multiple times in parallel.
     *
     * @param minNumberOfOperations The minimum number of times the operation should be performed.
     * @param numberOfThreads       The number of parallel threads in which the operation should be performed.
     * @param operation             The operation to be performed.
     * @param <R>                   The type of result of the operation to be performed.
     * @throws Exception If any Exception occurs while executing the Operation
     */
    @SuppressWarnings("ProhibitedExceptionDeclared")
    public static <R> Stream<R> stream(final int minNumberOfOperations,
                                       final int numberOfThreads,
                                       final Operation<R> operation) throws Exception {
        return report(minNumberOfOperations, numberOfThreads, operation).reThrow(Error.class)
                                                                        .reThrow(Exception.class)
                                                                        .stream();
    }

    /**
     * Returns a {@link Stream} of results after executing a particular operation multiple times in parallel.
     *
     * @param numberOfThreads       The number of parallel threads in which the operation should be performed.
     * @param operation             The operation to be performed.
     * @param <R>                   The type of result of the operation to be performed.
     * @throws Exception If any Exception occurs while executing the Operation
     */
    @SuppressWarnings("ProhibitedExceptionDeclared")
    public static <R> Stream<R> stream(final int numberOfThreads, final Operation<R> operation) throws Exception {
        return stream(0, numberOfThreads, operation);
    }

    private Thread newThread(final int threadIndex, final Operation<R> operation) {
        //noinspection ObjectToString
        return new Thread(newRunnable(threadIndex, operation), this + ":" + threadIndex);
    }

    @SuppressWarnings({"BoundedWildcard", "OverlyBroadCatchBlock"})
    private Runnable newRunnable(final int threadIndex, final Operation<R> operation) {
        return () -> {
            final int executionIndex = executionCounter.getAndIncrement();
            do {
                try {
                    report.add(operation.operate(new Indices(threadIndex,
                                                             executionIndex,
                                                             operationCounter.getAndIncrement())));
                } catch (final Throwable e) {
                    report.add(e);
                }
            } while (unfinished());
        };
    }

    private boolean unfinished() {
        return (executionCounter.get() < threads.size()) || (operationCounter.get() < minNumberOfOperations);
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
