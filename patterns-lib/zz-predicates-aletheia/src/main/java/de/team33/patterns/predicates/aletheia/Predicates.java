package de.team33.patterns.predicates.aletheia;

import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * A utility that provides special {@link Predicate}s.
 */
public final class Predicates {

    @SuppressWarnings("rawtypes")
    private static final Predicate ACCEPT;

    @SuppressWarnings("rawtypes")
    private static final Predicate REJECT;

    static {
        //noinspection rawtypes,AnonymousInnerClassWithTooManyMethods,OverlyComplexAnonymousInnerClass
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

        //noinspection rawtypes,AnonymousInnerClassWithTooManyMethods,OverlyComplexAnonymousInnerClass
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

    /**
     * Returns a {@link Predicate} such that {@code and(p1, p2, p3, ...).}{@link Predicate#and(Predicate) test(anything)}
     * returns the same as {@code Stream.of(p1, p2, p3, ...).allMatch(p -> p.test(anything))}.
     * <p>
     * Returns {@link #reject()} if any of the given <em>predicates</em> is {@link #reject()}.
     * <p>
     * Furthermore, ...
     * <ul>
     *      <li>{@code and(p1, p2, p3, ...).and}({@link #reject()}) always returns {@link #reject()}.</li>
     *      <li>{@link Predicate#and(Predicate) and(p1, p2, p3).and(accept())} returns same as {@code and(p1, p2, p3)}.</li>
     *      <li>{@link Predicate#and(Predicate) and(p1, p2, p3).and(and(p4, p5, p6))} returns same as
     *      {@code and(p1, p2, p3, p4, p5, p6)}.</li>
     *      <li>{@link Predicate#or(Predicate) reject().or(other)} always returns {@code other}.</li>
     *      <li>{@link Predicate#negate() reject().negate()} always returns {@link #accept()}.</li>
     *  </ul>
     *
     * @throws NullPointerException if any of the given <em>predicates</em> is {@code null}.
     */
    @SafeVarargs
    public static <T> Predicate<T> and(final Predicate<? super T>... predicates) {
        return Stream.of(predicates)
                     .map(predicate -> new And<T>().and(predicate))
                     .reduce(Predicate::and)
                     .orElseGet(Predicates::accept);
    }
}
