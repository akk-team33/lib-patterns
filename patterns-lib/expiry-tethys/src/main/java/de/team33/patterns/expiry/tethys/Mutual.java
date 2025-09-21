package de.team33.patterns.expiry.tethys;

import de.team33.patterns.exceptional.dione.XSupplier;

import java.time.Duration;
import java.time.Instant;

class Mutual<T, X extends Exception> {

    private final Rule<? extends T, ? extends X> rule;
    private volatile Actual<T> actual = now -> true;

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
    Mutual(final XSupplier<? extends T, ? extends X> newSubject,
           final Duration maxIdle, final Duration maxLiving) {
        this(new Rule<>(newSubject, maxIdle, maxLiving));
    }

    @SuppressWarnings("DesignForExtension")
    T get() throws X {
        return approved(actual, Instant.now());
    }

    private T approved(final Actual<? extends T> candidate, final Instant now) throws X {
        return candidate.isTimeout(now) ? updated(candidate, now) : candidate.get(now);
    }

    @SuppressWarnings("ObjectEquality")
    private T updated(final Actual<? extends T> outdated, final Instant now) throws X {
        final T result;
        synchronized (rule) {
            if (actual == outdated) {
                actual = new Substantial(now);
            }
            result = actual.get(now);
        }
        return result;
    }

    @SuppressWarnings("InterfaceWithOnlyOneDirectInheritor")
    @FunctionalInterface
    private interface Actual<T> {

        boolean isTimeout(final Instant now);

        default T get(final Instant now) {
            throw new UnsupportedOperationException("method not supported");
        }
    }

    private class Substantial implements Actual<T> {

        private final T subject;
        private final Instant lifeTimeout;
        private volatile Instant idleTimeout;

        Substantial(final Instant now) throws X {
            this.lifeTimeout = now.plus(rule.maxLife);
            this.idleTimeout = now.plus(rule.maxIdle);
            this.subject = rule.newSubject.get();
        }

        @Override
        public final boolean isTimeout(final Instant now) {
            return now.isAfter(lifeTimeout) || now.isAfter(idleTimeout);
        }

        @Override
        public final T get(final Instant now) {
            idleTimeout = now.plus(rule.maxIdle);
            return subject;
        }
    }

    private record Rule<T, X extends Exception>(XSupplier<? extends T, ? extends X> newSubject,
                                                Duration maxIdle,
                                                Duration maxLife) {
    }
}
