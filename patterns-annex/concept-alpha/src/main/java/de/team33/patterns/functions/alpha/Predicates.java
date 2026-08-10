package de.team33.patterns.functions.alpha;

import java.util.function.Predicate;

/**
 * A utility that provides special {@link Predicate}s.
 */
public final class Predicates {

    @SuppressWarnings("rawtypes")
    private static final Predicate ACCEPT;

    @SuppressWarnings("rawtypes")
    private static final Predicate REJECT;

    static {
        //noinspection rawtypes
        ACCEPT = new Predicate() {

            @Override
            public boolean test(final Object any) {
                return true;
            }

            @Override
            public Predicate and(final Predicate other) {
                return other;
            }

            @Override
            public Predicate or(final Predicate other) {
                return this;
            }

            @Override
            public Predicate negate() {
                return REJECT;
            }

            @Override
            public String toString() {
                return "ACCEPT";
            }
        };

        //noinspection rawtypes
        REJECT = new Predicate() {

            @Override
            public boolean test(final Object any) {
                return false;
            }

            @Override
            public Predicate and(final Predicate other) {
                return this;
            }

            @Override
            public Predicate or(final Predicate other) {
                return other;
            }

            @Override
            public Predicate negate() {
                return ACCEPT;
            }

            @Override
            public String toString() {
                return "REJECT";
            }
        };
    }

    private Predicates() {
    }

    /**
     * Returns a singleton {@link Predicate} such that {@link Predicate#test(Object) accept().test(anything)}
     * always returns {@code true}.
     * <p>
     * Furthermore, ...
     * <ul>
     *     <li>{@link Predicate#and(Predicate) accept().and(other)} always returns {@code other}.</li>
     *     <li>{@link Predicate#or(Predicate) accept().or(other)} always returns {@code accept()}.</li>
     *     <li>{@link Predicate#negate() accept().negate()} always returns {@link #reject()}.</li>
     * </ul>
     */
    @SuppressWarnings("unchecked")
    public static <T> Predicate<T> accept() {
        return ACCEPT;
    }

    /**
     * Returns a singleton {@link Predicate} such that {@link Predicate#test(Object) reject().test(anything)}
     * always returns {@code false}.
     * <p>
     * Furthermore, ...
     * <ul>
     *      <li>{@link Predicate#and(Predicate) reject().and(other)} always returns {@code reject()}.</li>
     *      <li>{@link Predicate#or(Predicate) reject().or(other)} always returns {@code other}.</li>
     *      <li>{@link Predicate#negate() reject().negate()} always returns {@link #accept()}.</li>
     *  </ul>
     */
    @SuppressWarnings("unchecked")
    public static <T> Predicate<T> reject() {
        return REJECT;
    }
}
