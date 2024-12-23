package de.team33.patterns.execution.metis;

import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

import static java.util.logging.Level.FINE;
import static java.util.logging.Level.WARNING;

public class SimpleAsyncExecutor implements Executor {

    private static final Logger LOG = Logger.getLogger(SimpleAsyncExecutor.class.getCanonicalName());
    private static final AtomicLong NEXT_INDEX = new AtomicLong(0L);

    private final String namePrefix = getClass().getCanonicalName() + "#" + NEXT_INDEX.getAndIncrement() + "#";
    private final AtomicLong anyIndex = new AtomicLong(0L);

    @Override
    public final void execute(final Runnable command) {
        final String name = anyName();
        new Thread(() -> run(command, name), name).start();
    }

    private static void run(final Runnable command, final String name) {
        try {
            command.run();
            LOG.log(FINE, () -> "Thread terminated normal: " + name);
        } catch (final Error | RuntimeException e) {
            LOG.log(WARNING, e, () -> "Thread terminated exceptional: " + name);
            throw e;
        }
    }

    private String anyName() {
        return namePrefix + anyIndex.getAndIncrement();
    }
}
