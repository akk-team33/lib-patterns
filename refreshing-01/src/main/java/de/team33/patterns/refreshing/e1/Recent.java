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

    private final Rule<T> rule;
    private volatile Actual<T> actual;

    /**
     * Initializes a new instance given a {@link Rule}.
     *
     * @see #rule(Supplier, long)
     * @see #rule(Supplier, Consumer, long)
     */
    public Recent(final Rule<T> rule) {
        this.rule = rule;
        this.actual = new Actual<>(null, Long.MIN_VALUE);
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

    @Override
    public final T get() {
        return approved(actual, System.currentTimeMillis());
    }

    private T approved(final Actual<? extends T> candidate, final long now) {
        return (now > candidate.timeout) ? updated(candidate.subject, now) : candidate.subject;
    }

    private synchronized T updated(final T outdated, final long now) {
        if (outdated == actual.subject) {
            actual = new Actual<>(rule.newSubject.get(), now + rule.lifeSpan);
            if (null != outdated) rule.oldSubject.accept(outdated);
        }
        return actual.subject;
    }

    private static class Actual<T> {

        final T subject;
        final long timeout;

        Actual(final T subject, final long timeout) {
            this.subject = subject;
            this.timeout = timeout;
        }
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
