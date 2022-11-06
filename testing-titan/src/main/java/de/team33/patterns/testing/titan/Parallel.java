package de.team33.patterns.testing.titan;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toList;

/**
 * A tool/utility to execute an {@link Operation} multiple times in parallel for test purposes.
 */
public final class Parallel<R> {

    private final int minNumberOfOperations;
    private final int maxNumberOfOperations;
    private final List<Thread> threads;
    private final Report.Builder<R> report = new Report.Builder<>();
    private final AtomicInteger operationCounter = new AtomicInteger(0);
    private final AtomicInteger executionCounter = new AtomicInteger(0);

    private Parallel(final Condition condition, final Operation<R> operation) {
        this.minNumberOfOperations = condition.getMinNumberOfOperations();
        this.maxNumberOfOperations = condition.getMaxNumberOfOperations();
        this.threads = unmodifiableList(IntStream.range(0, condition.getNumberOfThreads())
                                                 .mapToObj(threadIndex -> newThread(threadIndex, operation))
                                                 .collect(toList()));
    }

    /**
     * Returns a {@link Report} after executing a particular operation multiple times in parallel.
     *
     * @param condition The {@link Condition} under which the operation should be performed.
     * @param operation The operation to be performed.
     * @param <R>       The type of result of the operation to be performed.
     * @see #report(int, Operation)
     */
    public static <R> Report<R> report(final Condition condition, final Operation<R> operation) {
        return new Parallel<R>(condition, operation).startThreads()
                                                    .joinThreads()
                                                    .report();
    }

    /**
     * Returns a {@link Report} after executing a particular operation multiple times in parallel.
     *
     * @param numberOfThreads The number of parallel threads in which the operation should be performed.
     * @param operation       The operation to be performed.
     * @param <R>             The type of result of the operation to be performed.
     * @see #report(Condition, Operation)
     */
    public static <R> Report<R> report(final int numberOfThreads, final Operation<R> operation) {
        return report(Condition.byThreads(numberOfThreads), operation);
    }

    /**
     * Returns a {@link Stream} of results after executing a particular operation multiple times in parallel.
     *
     * @param condition The number of parallel threads in which the operation should be performed.
     * @param operation The operation to be performed.
     * @param <R>       The type of result of the operation to be performed.
     * @throws Exception If any Exception occurs while executing the Operation
     */
    @SuppressWarnings("ProhibitedExceptionDeclared")
    public static <R> Stream<R> stream(final Condition condition, final Operation<R> operation) throws Exception {
        return report(condition, operation).reThrow(Error.class)
                                           .reThrow(Exception.class)
                                           .stream();
    }

    /**
     * Returns a {@link Stream} of results after executing a particular operation multiple times in parallel.
     *
     * @param numberOfThreads The number of parallel threads in which the operation should be performed.
     * @param operation       The operation to be performed.
     * @param <R>             The type of result of the operation to be performed.
     * @throws Exception If any Exception occurs while executing the Operation
     */
    @SuppressWarnings("ProhibitedExceptionDeclared")
    public static <R> Stream<R> stream(final int numberOfThreads, final Operation<R> operation) throws Exception {
        return stream(Condition.byThreads(numberOfThreads), operation);
    }

    private Thread newThread(final int threadIndex, final Operation<R> operation) {
        //noinspection ObjectToString
        return new Thread(newRunnable(threadIndex, operation), this + ":" + threadIndex);
    }

    private int nextOpIndex() {
        return operationCounter.getAndIncrement();
    }

    private boolean maintain(final int opIndex, final int loopIndex) {
        return (opIndex < maxNumberOfOperations) && ((0 == loopIndex) || (opIndex < minNumberOfOperations));
    }

    @SuppressWarnings({"BoundedWildcard", "OverlyBroadCatchBlock"})
    private Runnable newRunnable(final int threadIndex, final Operation<R> operation) {
        return () -> {
            final int executionIndex = executionCounter.getAndIncrement();
            for (int i = nextOpIndex(), k = 0; maintain(i, k); i = nextOpIndex(), ++k) {
                try {
                    report.add(operation.operate(new Input(threadIndex, executionIndex, i, k)));
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
