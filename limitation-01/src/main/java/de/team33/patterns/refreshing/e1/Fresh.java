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

    private final Rule<T> rule;
    private volatile long timeout = Long.MIN_VALUE;
    private volatile T current = null;

    /**
     * Initializes a new instance given a {@link Rule}.
     */
    public Fresh(final Rule<T> rule) {
        this.rule = rule;
    }

    @Override
    public final T get() {
        final long now = System.currentTimeMillis();
        return (now > timeout) ? updated(current, now) : current;
    }

    private synchronized T updated(final T outdated, final long now) {
        if (current == outdated) {
            current = rule.nextSubject();
            timeout = now + rule.lifetime;
            rule.close(outdated);
        }
        return current;
    }

    /**
     * Summarizes the immutable properties of a {@link Fresh}.
     *
     * @param <T> The type of instances to be handled (of a {@link Fresh}).
     */
    public static class Rule<T> {

        @SuppressWarnings("rawtypes")
        private static final Consumer PLAIN_OLD_SUBJECT = any -> {
        };

        final Supplier<T> newSubject;
        final Consumer<T> oldSubject;
        final long lifetime;

        /**
         * Initializes a new instance (of this type) given a method for creating a new instance to handle and their
         * respective lifetimes in milliseconds.
         * <p>
         * This variant is intended for handleable instances that do not require special deinitialization if they are
         * to be discarded.
         *
         * @see #Rule(Supplier, Consumer, long)
         */
        public Rule(final Supplier<T> newSubject, final long lifetime) {
            this(newSubject, plainOldSubject(), lifetime);
        }

        /**
         * Initializes a new instance (of this type) based on a method for creating a new and discarding a used
         * instance to handle and their respective lifetimes in milliseconds.
         * <p>
         * This variant is intended for handleable instances that require special deinitialization if they are to be
         * discarded.
         *
         * @see #Rule(Supplier, long)
         */
        public Rule(final Supplier<T> newSubject, final Consumer<T> oldSubject, final long lifetime) {
            this.newSubject = newSubject;
            this.oldSubject = oldSubject;
            this.lifetime = lifetime;
        }

        @SuppressWarnings("unchecked")
        private static <T> Consumer<T> plainOldSubject() {
            return PLAIN_OLD_SUBJECT;
        }

        final T nextSubject() {
            return newSubject.get();
        }

        final void close(final T outdated) {
            if (null != outdated) {
                oldSubject.accept(outdated);
            }
        }
    }
}
