package de.team33.patterns.decision.thyone;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;

public abstract class Choices<I> {

    @SuppressWarnings("OverloadedVarargsMethod")
    @SafeVarargs
    public static <I> Choices<I> serial(final Predicate<I>... criteria) {
        return new Serial<>(List.of(criteria));
    }

    @SuppressWarnings({"WeakerAccess", "unused"})
    public static <I> Choices<I> serial(final List<? extends Predicate<? super I>> criteria) {
        return new Serial<>(List.copyOf(criteria));
    }

    @SuppressWarnings("OverloadedVarargsMethod")
    @SafeVarargs
    public static <I> Choices<I> parallel(final Predicate<I>... criteria) {
        return new Parallel<>(List.of(criteria));
    }

    @SuppressWarnings({"WeakerAccess", "unused"})
    public static <I> Choices<I> parallel(final List<? extends Predicate<? super I>> criteria) {
        return new Parallel<>(List.copyOf(criteria));
    }

    public abstract int apply(final I input);

    public <R> Function<I, R> mapping(final IntFunction<? extends R> function) {
        return input -> function.apply(apply(input));
    }

    @SuppressWarnings("OverloadedVarargsMethod")
    @SafeVarargs
    public final <R> Function<I, R> replying(final R... results) {
        return replying(Arrays.asList(results));
    }

    @SuppressWarnings("WeakerAccess")
    public <R> Function<I, R> replying(final List<? extends R> results) {
        final List<R> myResults = new ArrayList<>(results);
        return mapping(index -> {
            try {
                return myResults.get(index);
            } catch (final IndexOutOfBoundsException e) {
                throw new IllegalArgumentException(("No result defined with index %d%n" +
                                                    "    available results: %s").formatted(index, myResults), e);
            }
        });
    }

    private static final class Parallel<I> extends Choices<I> {

        private final List<Predicate<? super I>> criteria;

        private Parallel(final List<Predicate<? super I>> criteria) {
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

        private final List<Predicate<? super I>> criteria;

        private Serial(final List<Predicate<? super I>> criteria) {
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
