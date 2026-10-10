package de.team33.patterns.predicates.aletheia;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
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
        //noinspection rawtypes,AnonymousInnerClassWithTooManyMethods,OverlyComplexAnonymousInnerClass
        ACCEPT = new Predicate() {

            @Override
            public boolean test(final Object any) {
                return true;
            }

            @Override
            public Predicate and(final Predicate other) {
                return Objects.requireNonNull(other);
            }

            @Override
            public Predicate or(final Predicate other) {
                Objects.requireNonNull(other);
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
                Objects.requireNonNull(other);
                return this;
            }

            @Override
            public Predicate or(final Predicate other) {
                return Objects.requireNonNull(other);
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
     * Furthermore, if {@code other} is not {@code null}:
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
     * Furthermore, if {@code other} is not {@code null}:
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
     * Returns a {@link Predicate} that combines the given <em>predicates</em> using
     * {@link Predicate#and(Predicate) and}.
     * <p>
     * Returns {@link #accept()} if no <em>predicates</em> are given.
     * <p>
     * Returns {@link #reject()} if the given <em>predicates</em> contain {@link #reject()}.
     */
    @SafeVarargs
    public static <T> Predicate<T> and(final Predicate<? super T>... predicates) {
        //noinspection RedundantTypeArguments
        return and(List.<Predicate<? super T>>of(predicates));
    }

    /**
     * Returns a {@link Predicate} that combines the given <em>predicates</em> using
     * {@link Predicate#and(Predicate) and}.
     * <p>
     * Returns {@link #accept()} if no <em>predicates</em> are given.
     * <p>
     * Returns {@link #reject()} if the given <em>predicates</em> contain {@link #reject()}.
     */
    @SuppressWarnings("unchecked")
    public static <T> Predicate<T> and(final Collection<? extends Predicate<? super T>> predicates) {
        return predicates.stream()
                         .filter(predicate -> predicate != accept())
                         .map(predicate -> (Predicate<T>) predicate)
                         .reduce(accept(), Predicates::andOrReject);
    }

    private static <T> Predicate<T> andOrReject(final Predicate<T> left, final Predicate<? super T> right) {
        return (right == reject()) ? reject() : left.and(right);
    }

    /**
     * Returns a {@link Predicate} that combines the given <em>predicates</em> using
     * {@link Predicate#or(Predicate) or}.
     * <p>
     * Returns {@link #reject()} if no <em>predicates</em> are given.
     * <p>
     * Returns {@link #accept()} if the given <em>predicates</em> contain {@link #accept()}.
     */
    @SafeVarargs
    public static <T> Predicate<T> or(final Predicate<? super T>... predicates) {
        //noinspection RedundantTypeArguments
        return or(List.<Predicate<? super T>>of(predicates));
    }

    /**
     * Returns a {@link Predicate} that combines the given <em>predicates</em> using
     * {@link Predicate#or(Predicate) or}.
     * <p>
     * Returns {@link #reject()} if no <em>predicates</em> are given.
     * <p>
     * Returns {@link #accept()} if the given <em>predicates</em> contain {@link #accept()}.
     */
    @SuppressWarnings("unchecked")
    public static <T> Predicate<T> or(final Collection<? extends Predicate<? super T>> predicates) {
        return predicates.stream()
                         .filter(predicate -> predicate != reject())
                         .map(predicate -> (Predicate<T>) predicate)
                         .reduce(reject(), Predicates::orOrAccept);
    }

    private static <T> Predicate<T> orOrAccept(final Predicate<T> left, final Predicate<? super T> right) {
        return (right == accept()) ? accept() : left.or(right);
    }
}
