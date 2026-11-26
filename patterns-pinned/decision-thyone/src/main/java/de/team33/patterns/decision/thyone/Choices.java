package de.team33.patterns.decision.thyone;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;

/**
 * Abstracts the choices resulting from a combination of {@linkplain Predicate boolean criteria}
 * with respect to an input of type {@code <I>}.
 * <p>
 * When {@linkplain #apply(Object) evaluating a specific input}, a single choice is represented
 * as an {@code int} value, which can be used as a decision criterion in, for example, a {@code switch} statement.
 * <p>
 * To get an instance, use either {@link #serial(Predicate[])}, {@link #serial(Collection)},
 * {@link #parallel(Predicate[])} or {@link #parallel(Collection)}.
 */
public abstract class Choices<I> {

    /**
     * Retrieves a new instance consisting of a series of boolean <em>criteria</em>.
     * <p>
     * When {@linkplain #apply(Object) evaluating a specific input}, the <em>criteria</em> will be
     * {@linkplain Predicate#test(Object) evaluated} just like in a series of {@code if-else}-statements ...
     * <pre>
     * <em>final int n = criteria.length;</em>
     * if (criteria[0].test(input)) {
     *     return 0;
     * } else if (criteria[1].test(input)) {
     *     return 1;
     * } else if (criteria[2].test(input)) {
     *     return 2;
     * } <em>[...]</em>
     *   else if (criteria[n-1].test(input)) {
     *     return n-1;
     * } else {
     *     return n;
     * }
     * </pre>
     * <p>
     * Let <em>n</em> be the number of given <em>criteria</em>, then <em>n+1</em> is the number of possible results
     * when {@linkplain #apply(Object) evaluating an input}.
     * More precisely, the result range is between <em>0</em> and <em>n</em> (inclusive).
     */
    @SuppressWarnings("OverloadedVarargsMethod")
    @SafeVarargs
    public static <I> Choices<I> serial(final Predicate<I>... criteria) {
        return new Serial<>(List.of(criteria));
    }

    /**
     * Retrieves a new instance consisting of a series of boolean <em>criteria</em>.
     * <p>
     * See {@link #serial(Predicate[])} for more details.
     *
     * @param criteria CAUTION: be careful with {@link Collection}s other than {@link List}s:
     *                 The effective number and order of elements will be significant!
     */
    public static <I> Choices<I> serial(final Collection<? extends Predicate<? super I>> criteria) {
        return new Serial<>(List.copyOf(criteria));
    }

    /**
     * Retrieves a new instance consisting of a combination of boolean <em>criteria</em>.
     * <p>
     * When {@linkplain #apply(Object) evaluating a specific input}, all criteria are always
     * {@linkplain Predicate#test(Object) evaluated} and each represented by a specific bit (MSB first)
     * of the resulting {@code int}, in principle as in the following case of three criteria ...
     * <pre>
     * if (criteria[0].test(input)) {
     *     if (criteria[1].test(input)) {
     *         if (criteria[2].test(input)) {
     *             return 0b111;
     *         } else {
     *             return 0b110;
     *         }
     *     } else {
     *         if (criteria[2].test(input)) {
     *             return 0b101;
     *         } else {
     *             return 0b100;
     *         }
     *     }
     * } else {
     *     if (criteria[1].test(input)) {
     *         if (criteria[2].test(input)) {
     *             return 0b011;
     *         } else {
     *             return 0b010;
     *         }
     *     } else {
     *         if (criteria[2].test(input)) {
     *             return 0b001;
     *         } else {
     *             return 0b000;
     *         }
     *     }
     * }
     * </pre>
     * <p>
     * Let <em>n</em> be the number of given <em>criteria</em>, then <em>2<sup>n</sup></em> is the number of possible
     * results when {@linkplain #apply(Object) evaluating an input}.
     * More precisely, the result range is between <em>0</em> and <em>2<sup>n</sup>-1</em> (inclusive).
     */
    @SuppressWarnings("OverloadedVarargsMethod")
    @SafeVarargs
    public static <I> Choices<I> parallel(final Predicate<I>... criteria) {
        return new Parallel<>(List.of(criteria));
    }

    /**
     * Retrieves a new instance consisting of a combination of boolean <em>criteria</em>.
     * <p>
     * See {@link #parallel(Predicate[])} for more details.
     *
     * @param criteria CAUTION: be careful with {@link Collection}s other than {@link List}s:
     *                 The effective number and order of elements will be significant!
     */
    public static <I> Choices<I> parallel(final Collection<? extends Predicate<? super I>> criteria) {
        return new Parallel<>(List.copyOf(criteria));
    }

    /**
     * Returns an {@code int} value representing the combined results of the associated
     * {@linkplain Predicate boolean criteria}.
     * <p>
     * The kind of combination and the result range depends on how <em>this</em> {@link Choices} were created.
     *
     * @see #serial(Predicate[])
     * @see #serial(Collection)
     * @see #parallel(Predicate[])
     * @see #parallel(Collection)
     */
    public abstract int apply(final I input);

    /**
     * Returns a {@link Function} that maps an input of type {@code <I>} to a result of type {@code <R>}
     * based on <em>this</em> {@link Choices} and a given <em>function</em> that maps an {@code int} to a result
     * of type {@code <R>}.
     */
    public final <R> Function<I, R> andThen(final IntFunction<? extends R> function) {
        return input -> function.apply(apply(input));
    }

    /**
     * Returns a {@link Function} that maps an input of type {@code <I>} to one of the given <em>results</em>
     * of type {@code <R>}.
     * <p>
     * The number of <em>results</em> should equal the number of possible results when
     * {@linkplain #apply(Object) evaluating an input}. The latter depends on how <em>this</em>
     * {@link Choices} were created.
     *
     * @see #serial(Predicate[])
     * @see #serial(Collection)
     * @see #parallel(Predicate[])
     * @see #parallel(Collection)
     */
    @SuppressWarnings("OverloadedVarargsMethod")
    @SafeVarargs
    public final <R> Function<I, R> replying(final R... results) {
        // avoid List.of() because <null> may be an eligible result! ...
        return replying(Arrays.asList(results));
    }

    /**
     * Returns a {@link Function} that maps an input of type {@code <I>} to one of the given <em>results</em>
     * of type {@code <R>}.
     * <p>
     * The number of <em>results</em> should equal the number of possible results when
     * {@linkplain #apply(Object) evaluating an input}. The latter depends on how <em>this</em>
     * {@link Choices} were created.
     *
     * @param results CAUTION: be careful with {@link Collection}s other than {@link List}s:
     *                The effective number and order of elements will be significant!
     * @see #serial(Predicate[])
     * @see #serial(Collection)
     * @see #parallel(Predicate[])
     * @see #parallel(Collection)
     */
    public final <R> Function<I, R> replying(final Collection<? extends R> results) {
        final List<R> resultList = new ArrayList<>(results);
        return andThen(index -> {
            try {
                return resultList.get(index);
            } catch (final IndexOutOfBoundsException e) {
                throw new IllegalArgumentException(("No result defined with index <%d>%n" +
                                                    "    available results: %s").formatted(index, resultList), e);
            }
        });
    }

    /**
     * Returns a {@link Function} that maps an input of type {@code <I>} to one of the given <em>methods</em>
     * to finally get a result of type {@code <R>}.
     * <p>
     * The number of <em>results</em> should equal the number of possible results when
     * {@linkplain #apply(Object) evaluating an input}. The latter depends on how <em>this</em>
     * {@link Choices} were created.
     *
     * @see #serial(Predicate[])
     * @see #serial(Collection)
     * @see #parallel(Predicate[])
     * @see #parallel(Collection)
     */
    @SuppressWarnings("OverloadedVarargsMethod")
    @SafeVarargs
    public final <R> Function<I, R> applying(final Function<? super I, ? extends R>... methods) {
        // Using List.of() because a useful method cannot be <null> ...
        return applying(List.of(methods));
    }

    /**
     * Returns a {@link Function} that maps an input of type {@code <I>} to one of the given <em>methods</em>
     * to finally get a result of type {@code <R>}.
     * <p>
     * The number of <em>methods</em> should equal the number of possible results when
     * {@linkplain #apply(Object) evaluating an input}. The latter depends on how <em>this</em>
     * {@link Choices} were created.
     *
     * @param methods CAUTION: be careful with {@link Collection}s other than {@link List}s:
     *                The effective number and order of elements will be significant!
     * @see #serial(Predicate[])
     * @see #serial(Collection)
     * @see #parallel(Predicate[])
     * @see #parallel(Collection)
     */
    public final <R> Function<I, R> applying(final Collection<? extends Function<? super I, ? extends R>> methods) {
        final Function<I, ? extends Function<? super I, ? extends R>> replying = replying(methods);
        return input -> replying.apply(input)
                                .apply(input);
    }

    private static final class Parallel<I> extends Choices<I> {

        private final List<? extends Predicate<? super I>> criteria;

        private Parallel(final List<? extends Predicate<? super I>> criteria) {
            this.criteria = criteria;
        }

        @Override
        public int apply(final I input) {
            return criteria.stream()
                           .mapToInt(criterion -> criterion.test(input) ? 1 : 0)
                           .reduce(0, ((left, right) -> (left << 1) + right));
        }
    }

    private static final class Serial<I> extends Choices<I> {

        private final List<? extends Predicate<? super I>> criteria;

        private Serial(final List<? extends Predicate<? super I>> criteria) {
            this.criteria = criteria;
        }

        @Override
        public int apply(final I input) {
            final int size = criteria.size();
            for (int index = 0; index < size; ++index) {
                if (criteria.get(index).test(input)) {
                    return index;
                }
            }
            return size;
        }
    }
}
