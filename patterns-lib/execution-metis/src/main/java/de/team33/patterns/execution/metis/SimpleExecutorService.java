package de.team33.patterns.execution.metis;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

import static java.util.logging.Level.FINE;
import static java.util.logging.Level.WARNING;

public class SimpleExecutorService extends AbstractExecutorService {

    private static final Logger LOG = Logger.getLogger(SimpleExecutorService.class.getCanonicalName());
    private static final AtomicLong NEXT_INDEX = new AtomicLong(0L);

    private final String namePrefix = getClass().getCanonicalName() + "#" + NEXT_INDEX.getAndIncrement() + "#";
    private final AtomicLong nextIndex = new AtomicLong(0L);
    private final Condition condition = new Condition();

    @Override
    public final void shutdown() {
        condition.shutdown();
    }

    @Override
    public final List<Runnable> shutdownNow() {
        condition.shutdown();
        return Collections.emptyList();
    }

    @Override
    public final boolean isShutdown() {
        return condition.isShutdown();
    }

    @Override
    public final boolean isTerminated() {
        return condition.isTerminated();
    }

    @Override
    public final boolean awaitTermination(final long timeout, final TimeUnit unit) throws InterruptedException {
        condition.awaitTermination(System.currentTimeMillis(), unit.toMillis(timeout));
        return condition.isTerminated();
    }

    @Override
    public final void execute(final Runnable command) {
        if (condition.isNormal()) {
            final String name = nextThreadName();
            new Thread(() -> run(command, name), name).start();
        } else {
            LOG.log(WARNING, () -> "this executor service is terminated - command ignored: " + command);
        }
    }

    private String nextThreadName() {
        return namePrefix + nextIndex.getAndIncrement();
    }

    private void run(final Runnable command, final String name) {
        condition.increment();
        try {
            command.run();
            LOG.log(FINE, () -> "Thread terminates normal: " + name);
        } catch (final Error | RuntimeException e) {
            LOG.log(WARNING, e, () -> "Thread terminates exceptional: " + name);
            throw e;
        } finally {
            condition.decrement();
        }
    }

    private enum State {
        NORMAL,
        SHUTDOWN
    }

    @SuppressWarnings({"SynchronizedMethod", "SynchronizeOnThis"})
    private static class Condition {

        private final AtomicLong counter = new AtomicLong(0L);
        private volatile State state = State.NORMAL;

        final boolean isNormal() {
            return State.NORMAL == state;
        }

        final boolean isShutdown() {
            return State.SHUTDOWN == state;
        }

        final boolean isTerminated() {
            return isShutdown() && (0L == counter.get());
        }

        final synchronized void shutdown() {
            state = State.SHUTDOWN;
            notifyAll();
        }

        final synchronized void awaitTermination(final long time0,
                                                 final long timeoutMillis) throws InterruptedException {
            for (long delta = 0;
                 (delta < timeoutMillis) && !isTerminated();
                 delta = System.currentTimeMillis() - time0) {
                wait(timeoutMillis - delta);
            }
        }

        final void increment() {
            counter.incrementAndGet();
        }

        final synchronized void decrement() {
            counter.decrementAndGet();
            notifyAll();
        }
    }
}
