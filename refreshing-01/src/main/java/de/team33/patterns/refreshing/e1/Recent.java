// TODO refreshing -> expiry
package de.team33.patterns.refreshing.e1;

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
        public boolean isTimeout(final long now) {
            return true;
        }

        @Override
        public Object get() {
            throw new IllegalStateException("not available in initial state");
        }
    };

    private final Rule<T> rule;
    private volatile Actual<T> actual = initial();

    /**
     * Initializes a new instance given a {@link Rule}.
     * <p>
     * CAUTION: The life span given by the {@link Rule} should be significantly smaller than the actually expected
     * life span of an instance to be handled, otherwise there may not be enough time to use a
     * {@linkplain #get() provided} instance successfully!
     *
     * @see #rule(Supplier, long)
     */
    public Recent(final Rule<T> rule) {
        this.rule = rule;
    }

    public Recent(final Supplier<? extends T> newSubject, final long lifeSpan) {
        this(rule(newSubject, lifeSpan));
    }

    /**
     * Returns a new {@link Rule} given a method for creating a new instance to handle and their respective life span
     * in milliseconds.
     */
    public static <T> Rule<T> rule(final Supplier<? extends T> newSubject, final long lifeSpan) {
        return new Rule<>(newSubject, lifeSpan);
    }

    @SuppressWarnings("unchecked")
    private static <T> Actual<T> initial() {
        return INITIAL;
    }

    private static <T> Actual<T> substantial(final T subject, final long timeout) {
        return new Actual<T>() {
            @Override
            public boolean isTimeout(final long now) {
                return now > timeout;
            }

            @Override
            public T get() {
                return subject;
            }
        };
    }

    @Override
    public final T get() {
        return approved(actual, System.currentTimeMillis());
    }

    private T approved(final Actual<? extends T> candidate, final long now) {
        return candidate.isTimeout(now) ? updated(candidate, now) : candidate.get();
    }

    private synchronized T updated(final Actual<? extends T> outdated, final long now) {
        if (outdated == actual) {
            actual = substantial(rule.newSubject.get(), now + rule.lifeSpan);
        }
        return actual.get();
    }

    private interface Actual<T> {

        boolean isTimeout(final long now);

        T get();
    }

    /**
     * Summarizes the immutable properties of a {@link Recent}.
     *
     * @param <T> The type of instances to be handled (of a {@link Recent}).
     * @see #rule(Supplier, long)
     */
    public static final class Rule<T> {

        private final Supplier<? extends T> newSubject;
        private final long lifeSpan;

        private Rule(final Supplier<? extends T> newSubject, final long lifeSpan) {
            this.newSubject = newSubject;
            this.lifeSpan = lifeSpan;
        }
    }
}
