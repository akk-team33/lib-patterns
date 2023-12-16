package de.team33.patterns.expiry.tethys;

import de.team33.patterns.exceptional.dione.XSupplier;

import java.time.Instant;

class Mutual<T, X extends Exception> {

    private final Rule<? extends T, ? extends X> rule;
    private volatile Actual<T, X> actual = now -> true;

    private Mutual(final Rule<? extends T, ? extends X> rule) {
        this.rule = rule;
    }

    /**
     * Initializes a new instance of this container type given a {@link XSupplier} for the type to be handled and
     * an intended lifetime of such instances.
     * <p>
     * CAUTION: The given lifetime should be significantly smaller than the actually expected
     * life span of an instance to be handled, otherwise there may not be enough time to use a
     * {@linkplain #get() provided} instance successfully!
     *
     * @param newSubject the {@link XSupplier} for the instances to handle
     * @param maxIdle    the maximum idle time in milliseconds
     * @param maxLiving  the maximum lifetime in milliseconds
     */
    Mutual(final XSupplier<? extends T, ? extends X> newSubject, final long maxIdle, final long maxLiving) {
        this(new Rule<>(newSubject, maxIdle, maxLiving));
    }

    T get() throws X {
        return approved(actual);
    }

    private T approved(final Actual<? extends T, ? extends X> candidate) throws X {
        return candidate.isTimeout(Instant.now()) ? updated(candidate) : candidate.get();
    }

    @SuppressWarnings("ObjectEquality")
    private T updated(final Actual<? extends T, ? extends X> outdated) throws X {
        final T result;
        synchronized (rule) {
            if (actual == outdated) {
                actual = new Substantial();
            }
            result = actual.get();
        }
        return result;
    }

    @FunctionalInterface
    private interface Actual<T, X extends Exception> {

        boolean isTimeout(final Instant now);

        default T get() throws X {
            throw new UnsupportedOperationException("method not supported");
        }
    }

    private class Substantial implements Actual<T, X> {

        private final T subject;
        private final Instant lifeTimeout;
        private volatile Instant idleTimeout;

        Substantial() throws X {
            final Instant now = Instant.now();
            this.lifeTimeout = now.plusMillis(rule.maxLife);
            this.idleTimeout = now.plusMillis(rule.maxIdle);
            this.subject = rule.newSubject.get();
        }

        @Override
        public final boolean isTimeout(final Instant now) {
            return (now.compareTo(lifeTimeout) > 0) || (now.compareTo(idleTimeout) > 0);
        }

        @Override
        public final T get() {
            idleTimeout = Instant.now().plusMillis(rule.maxIdle);
            return subject;
        }
    }

    private static final class Rule<T, X extends Exception> {

        private final XSupplier<? extends T, ? extends X> newSubject;
        private final long maxLife;
        private final long maxIdle;

        Rule(final XSupplier<? extends T, ? extends X> newSubject, final long maxIdle, final long maxLife) {
            this.newSubject = newSubject;
            this.maxIdle = maxIdle;
            this.maxLife = maxLife;
        }
    }
}
