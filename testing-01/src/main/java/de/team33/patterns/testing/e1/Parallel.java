package de.team33.patterns.testing.e1;

import de.team33.patterns.exceptional.e1.XConsumer;
import de.team33.patterns.exceptional.e1.XFunction;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

/**
 * A tool/utility to execute operations multiple times in parallel for test purposes.
 */
public final class Parallel<R> {

    private final List<Thread> threads;
    private final Report.Builder<R> report = new Report.Builder<>();
    private final AtomicInteger executionCounter = new AtomicInteger(0);
    private final int numberOfExecutions;

    private Parallel(final int numberOfExecutions, final int numberOfThreads, final XFunction<Integer, R, ?> method) {
        this.numberOfExecutions = numberOfExecutions;
        this.threads = IntStream.range(0, numberOfThreads)
                                .mapToObj(threadIndex -> newThread(threadIndex, method))
                                .collect(toList());
    }

    /**
     * Returns a {@link Report} after executing a particular operation multiple times in parallel.
     *
     * @throws InterruptedException When the current thread is interrupted while waiting for the executing threads.
     * @see #get(int, XFunction)
     * @see #run(int, int, XConsumer)
     * @see #run(int, XConsumer)
     */
    public static <R> Report<R> get(final int numberOfExecutions,
                                    final int numberOfThreads,
                                    final XFunction<Integer, R, ?> operation) throws InterruptedException {
        return new Parallel<R>(numberOfExecutions, numberOfThreads, operation).startThreads()
                                                                              .joinThreads()
                                                                              .report();
    }

    /**
     * Returns a {@link Report} after executing a particular operation multiple times in parallel.
     *
     * @throws InterruptedException When the current thread is interrupted while waiting for the executing threads.
     * @see #get(int, int, XFunction)
     * @see #run(int, int, XConsumer)
     * @see #run(int, XConsumer)
     */
    public static <R> Report<R> get(final int numberOfExecutionsInSeparateThreads,
                                    final XFunction<Integer, R, ?> operation) throws InterruptedException {
        return get(numberOfExecutionsInSeparateThreads, numberOfExecutionsInSeparateThreads, operation);
    }

    /**
     * Returns a {@link Report} after executing a particular operation multiple times in parallel.
     *
     * @throws InterruptedException When the current thread is interrupted while waiting for the executing threads.
     * @see #run(int, XConsumer)
     * @see #get(int, int, XFunction)
     * @see #get(int, XFunction)
     */
    public static Report<Void> run(final int numberOfExecutions,
                                   final int numberOfThreads,
                                   final XConsumer<Integer, ?> operation) throws InterruptedException {
        return get(numberOfExecutions, numberOfThreads, toXFunction(operation));
    }

    /**
     * Returns a {@link Report} after executing a particular operation multiple times in parallel.
     *
     * @throws InterruptedException When the current thread is interrupted while waiting for the executing threads.
     * @see #run(int, int, XConsumer)
     * @see #get(int, int, XFunction)
     * @see #get(int, XFunction)
     */
    public static Report<Void> run(final int numberOfExecutionsInSeparateThreads,
                                   final XConsumer<Integer, ?> operation) throws InterruptedException {
        return run(numberOfExecutionsInSeparateThreads, numberOfExecutionsInSeparateThreads, operation);
    }

    @SuppressWarnings("BoundedWildcard")
    private static <X extends Exception>
    XFunction<Integer, Void, X> toXFunction(final XConsumer<Integer, X> operation) {
        return index -> {
            operation.accept(index);
            return null;
        };
    }

    private Thread newThread(final int threadIndex, final XFunction<Integer, R, ?> method) {
        return new Thread(newRunnable(method), this + ":" + threadIndex);
    }

    @SuppressWarnings({"BoundedWildcard", "OverlyBroadCatchBlock"})
    private Runnable newRunnable(final XFunction<Integer, R, ?> method) {
        return () -> {
            int executionIndex = executionCounter.getAndIncrement();
            while (executionIndex < numberOfExecutions) {
                try {
                    report.add(method.apply(executionIndex));
                } catch (final Throwable e) {
                    report.add(e);
                }
                executionIndex = executionCounter.getAndIncrement();
            }
        };
    }

    private Parallel<R> startThreads() {
        for (final Thread thread : threads) {
            thread.start();
        }
        return this;
    }

    private Parallel<R> joinThreads() throws InterruptedException {
        for (final Thread thread : threads) {
            thread.join();
        }
        return this;
    }

    private Report<R> report() {
        return report.build();
    }
}
