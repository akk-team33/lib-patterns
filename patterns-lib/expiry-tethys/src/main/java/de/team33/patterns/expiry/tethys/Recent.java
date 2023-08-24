package de.team33.patterns.expiry.tethys;

import java.time.Instant;
import java.util.function.Supplier;

/**
 * Defines a container type for handling instances, which in principle can be defined globally and
 * reused over and over again, but have to be updated after a certain time.
 * <p>
 * <b>Problem to solve:</b>
 * <p>
 * There are objects that, due to their technical properties, are predestined to be initialized and made available once
 * throughout the application. In particular, they are state-free but relatively "expensive" to initialize. Due to
 * their semantics, however, they have to be renewed from time to time. An example of such an object could be an
 * authentication token.
 * <p>
 * This class serves to handle such objects and in particular their updating.
 *
 * @param <T> The type of instances to handle.
 */
public class Recent<T> implements Supplier<T> {

    private final Rule<? extends T> rule;
    private volatile Actual<T> actual = now -> true;

    private Recent(final Rule<? extends T> rule) {
        this.rule = rule;
    }

    /**
     * Initializes a new instance of this container type given a {@link Supplier} for the type to be handled and
     * an intended lifetime of such instances.
     * <p>
     * CAUTION: The given lifetime should be significantly smaller than the actually expected
     * life span of an instance to be handled, otherwise there may not be enough time to use a
     * {@linkplain #get() provided} instance successfully!
     *
     * @param newSubject the {@link Supplier} for the instances to handle
     * @param maxIdle    the maximum idle time in milliseconds
     * @param maxLiving  the maximum lifetime in milliseconds
     */
    public Recent(final Supplier<? extends T> newSubject, final long maxIdle, final long maxLiving) {
        this(new Rule<>(newSubject, maxIdle, maxLiving));
    }

    @Override
    public final T get() {
        return approved(actual);
    }

    private T approved(final Actual<? extends T> candidate) {
        return candidate.isTimeout(Instant.now()) ? updated(candidate) : candidate.get();
    }

    @SuppressWarnings("ObjectEquality")
    private T updated(final Actual<? extends T> outdated) {
        synchronized (rule) {
            if (actual == outdated) {
                actual = new Substantial();
            }
            return actual.get();
        }
    }

    @FunctionalInterface
    private interface Actual<T> {

        boolean isTimeout(final Instant now);

        default T get() {
            throw new UnsupportedOperationException("method not supported");
        }
    }

    private class Substantial implements Actual<T> {

        private final T subject;
        private final Instant lifeTimeout;
        private volatile Instant idleTimeout;

        Substantial() {
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

    private static final class Rule<T> {

        private final Supplier<? extends T> newSubject;
        private final long maxLife;
        private final long maxIdle;

        Rule(final Supplier<? extends T> newSubject, final long maxIdle, final long maxLife) {
            this.newSubject = newSubject;
            this.maxIdle = maxIdle;
            this.maxLife = maxLife;
        }
    }
}
