package de.team33.patterns.expiry.e1;

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

    @SuppressWarnings("rawtypes")
    private static final Actual INITIAL = new Actual() {
        @Override
        public boolean isTimeout(final Instant now) {
            return true;
        }

        @Override
        public Object get() {
            throw new IllegalStateException("not available in initial state");
        }
    };

    private final Rule<? extends T> rule;
    private volatile Actual<T> actual = initial();

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
     * @param lifetime   the lifetime in milliseconds
     */
    public Recent(final Supplier<? extends T> newSubject, final long lifetime) {
        this(new Rule<>(newSubject, lifetime));
    }

    @SuppressWarnings("unchecked")
    private static <T> Actual<T> initial() {
        return INITIAL;
    }

    private static <T> Actual<T> substantial(final T subject, final Instant timeout) {
        return new Actual<T>() {
            @Override
            public boolean isTimeout(final Instant now) {
                return now.compareTo(timeout) > 0;
            }

            @Override
            public T get() {
                return subject;
            }
        };
    }

    @Override
    public final T get() {
        return approved(actual, Instant.now());
    }

    private T approved(final Actual<? extends T> candidate, final Instant now) {
        return candidate.isTimeout(now) ? updated(candidate, now) : candidate.get();
    }

    private synchronized T updated(final Actual<? extends T> outdated, final Instant now) {
        if (actual == outdated) {
            actual = substantial(rule.newSubject.get(), now.plusMillis(rule.lifetime));
        }
        return actual.get();
    }

    private interface Actual<T> {

        boolean isTimeout(final Instant now);

        T get();
    }

    private static final class Rule<T> {

        private final Supplier<? extends T> newSubject;
        private final long lifetime;

        private Rule(final Supplier<? extends T> newSubject, final long lifetime) {
            this.newSubject = newSubject;
            this.lifetime = lifetime;
        }
    }
}
