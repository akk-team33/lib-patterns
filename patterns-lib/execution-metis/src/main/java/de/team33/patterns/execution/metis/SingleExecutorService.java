package de.team33.patterns.execution.metis;

import java.util.List;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.TimeUnit;

public class SingleExecutorService extends AbstractExecutorService {
    @Override
    public void shutdown() {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    public List<Runnable> shutdownNow() {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    public boolean isShutdown() {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    public boolean isTerminated() {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    public boolean awaitTermination(final long timeout, final TimeUnit unit) throws InterruptedException {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    public void execute(final Runnable command) {
        throw new UnsupportedOperationException("not yet implemented");
    }
}
