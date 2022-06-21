package de.team33.patterns.refreshing.e1;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Defines a container type to handle instances that can in principle be used again and again but have to be renewed
 * after a certain period of time.
 *
 * @param <T> The type of instances to handle.
 */
public class Fresh<T> implements Supplier<T> {

    @SuppressWarnings("rawtypes")
    private static final Consumer PLAIN_OLD_SUBJECT = subject -> {};

    private final Rule<T> rule;
    private volatile long timeout = Long.MIN_VALUE;
    private volatile T current = null;

    /**
     * Initializes a new instance given a {@link Rule}.
     */
    public Fresh(final Rule<T> rule) {
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
    public static <T> Rule<T> rule(final Supplier<T> newSubject, final long lifeSpan) {
        //noinspection unchecked
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
    public static <T> Rule<T> rule(final Supplier<T> newSubject,
                                   final Consumer<? super T> oldSubject,
                                   final long lifeSpan) {
        return new Rule<>(newSubject, oldSubject, lifeSpan);
    }

    @Override
    public final T get() {
        final long now = System.currentTimeMillis();
        return (now > timeout) ? updated(current, now) : current;
    }

    private synchronized T updated(final T outdated, final long now) {
        if (current == outdated) {
            current = rule.newSubject.get();
            timeout = now + rule.lifeSpan;
            rule.oldSubject.accept(outdated);
        }
        return current;
    }

    /**
     * Summarizes the immutable properties of a {@link Fresh}.
     *
     * @param <T> The type of instances to be handled (of a {@link Fresh}).
     */
    public static final class Rule<T> {

        final Supplier<? extends T> newSubject;
        final Consumer<? super T> oldSubject;
        final long lifeSpan;

        private Rule(final Supplier<? extends T> newSubject,
                     final Consumer<? super T> oldSubject,
                     final long lifeSpan) {
            this.newSubject = newSubject;
            this.oldSubject = oldSubject;
            this.lifeSpan = lifeSpan;
        }
    }
}
