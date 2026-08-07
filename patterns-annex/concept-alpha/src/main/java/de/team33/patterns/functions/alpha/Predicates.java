package de.team33.patterns.functions.alpha;

import java.util.function.Predicate;

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
     * Returns a singleton {@link Predicate} that accepts any input.
     */
    @SuppressWarnings("unchecked")
    public static <T> Predicate<T> accept() {
        return ACCEPT;
    }

    /**
     * Returns a singleton {@link Predicate} that rejects any input.
     */
    @SuppressWarnings("unchecked")
    public static <T> Predicate<T> reject() {
        return REJECT;
    }
}
