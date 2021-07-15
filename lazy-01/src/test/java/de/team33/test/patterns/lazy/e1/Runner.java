package de.team33.test.patterns.lazy.e1;

import de.team33.patterns.exceptional.e1.XRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class Runner<X extends Exception> {

    private final int count;
    private final XRunnable<X> xRunnable;
    private final List<Throwable> headCaught = new ArrayList<>(1);

    private Runner(final int count, final XRunnable<X> xRunnable) {
        this.count = count;
        this.xRunnable = xRunnable;
    }

    static <X extends Exception> Runner<X> parallel(final int count, final XRunnable<X> xRunnable) throws X {
        return new Runner<>(count, xRunnable).runParallel();
    }

    static <X extends Exception> Runner<X> sequential(final int count, final XRunnable<X> xRunnable) throws X {
        return new Runner<>(count, xRunnable).runSequential();
    }

    private Runner<X> runSequential() throws X {
        for (int i = 0; i < count; ++i) {
            try {
                xRunnable.run();
            } catch (final Throwable caught) {
                addCaught(caught);
            }
        }
        return reThrowCaughtIfPresent();
    }

    private Runner<X> runParallel() throws X {
        final List<Thread> threads = Stream.generate(() -> new Thread(runnable(xRunnable)))
                                           .limit(count)
                                           .collect(Collectors.toList());
        for (Thread thread : threads) {
            thread.start();
        }
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (final InterruptedException caught) {
                addCaught(caught);
            }
        }
        return reThrowCaughtIfPresent();
    }

    private Runner<X> reThrowCaughtIfPresent() throws X {
        final Throwable caught = (0 < headCaught.size()) ? headCaught.get(0) : null;
        if (caught instanceof Error) {
            throw (Error) caught;
        } else if (caught instanceof RuntimeException) {
            throw (RuntimeException) caught;
        } else if (caught != null) {
            // a checked exception must be of type <X> ...
            // noinspection unchecked
            throw (X) caught;
        }
        return this;
    }

    private Runnable runnable(final XRunnable<X> xRunnable) {
        return () -> {
            try {
                xRunnable.run();
            } catch (final Throwable caught) {
                addCaught(caught);
            }
        };
    }

    private void addCaught(final Throwable caught) {
        synchronized (headCaught) {
            if (0 < headCaught.size()) {
                headCaught.get(0).addSuppressed(caught);
            } else {
                headCaught.add(caught);
            }
        }
    }
}
