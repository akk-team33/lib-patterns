package de.team33.patterns.refreshing.e1;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Defines a container type to handle instances that can in principle be used again and again but have to be renewed
 * after a certain period of time.
 *
 * @param <T> The type of instances to handle.
 */
public class Recent<T> implements Supplier<T> {

    private static final Consumer<Object> PLAIN_OLD_SUBJECT = subject -> {
    };
    @SuppressWarnings("rawtypes")
    private static final Actual INITIAL = new Actual() {
        @Override
        public boolean isTimeout(final long now) {
            return true;
        }

        @Override
        public void quit(final Consumer method) {
            // nothing to do in initial state
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
     * @see #rule(Supplier, Consumer, long)
     */
    public Recent(final Rule<T> rule) {
        this.rule = rule;
    }

    /**
     * Returns a new {@link Rule} given a method for creating a new instance to handle and their respective life span
     * in milliseconds.
     * <p>
     * This variant is intended for handleable instances that do not require special deinitialization if they are
     * to be discarded.
     *
     * @see #rule(Supplier, Consumer, long)
     */
    public static <T> Rule<T> rule(final Supplier<? extends T> newSubject, final long lifeSpan) {
        return new Rule<>(newSubject, PLAIN_OLD_SUBJECT, lifeSpan);
    }

    /**
     * Returns a new {@link Rule} given a method for creating a new and discarding an old instance to handle
     * and their respective life span in milliseconds.
     * <p>
     * This variant is intended for handleable instances that require special deinitialization if they are to be
     * discarded.
     *
     * @see #rule(Supplier, long)
     */
    public static <T> Rule<T> rule(final Supplier<? extends T> newSubject,
                                   final Consumer<? super T> oldSubject,
                                   final long lifeSpan) {
        return new Rule<>(newSubject, oldSubject, lifeSpan);
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
            public void quit(final Consumer<? super T> method) {
                method.accept(subject);
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
            outdated.quit(rule.oldSubject);
        }
        return actual.get();
    }

    private interface Actual<T> {

        boolean isTimeout(final long now);

        void quit(final Consumer<? super T> method);

        T get();
    }

    /**
     * Summarizes the immutable properties of a {@link Recent}.
     *
     * @param <T> The type of instances to be handled (of a {@link Recent}).
     */
    public static final class Rule<T> {

        final Supplier<? extends T> newSubject;
        final Consumer<? super T> oldSubject;
        final long lifeSpan;

        Rule(final Supplier<? extends T> newSubject,
             final Consumer<? super T> oldSubject,
             final long lifeSpan) {
            this.newSubject = newSubject;
            this.oldSubject = oldSubject;
            this.lifeSpan = lifeSpan;
        }
    }
}
