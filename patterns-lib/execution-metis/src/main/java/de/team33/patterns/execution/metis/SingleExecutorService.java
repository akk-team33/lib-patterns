package de.team33.patterns.execution.metis;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
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

    private final BlockingQueue<Runnable> queue = new LinkedBlockingQueue<>();

    @Override
    public void execute(final Runnable command) {
        queue.add(command);
        startThreadIfNecessary();
    }

    private final List<Thread> threads = new LinkedList<>();

    private void startThreadIfNecessary() {
        synchronized (threads) {
            if (threads.isEmpty()) {
                final Thread thread = new Thread(() -> work(), name());
                threads.add(thread);
                thread.start();
            }
        }
    }

    private void work() {
        throw new UnsupportedOperationException("not yet implemented");
    }

    private String name() {
        throw new UnsupportedOperationException("not yet implemented");
    }
}
