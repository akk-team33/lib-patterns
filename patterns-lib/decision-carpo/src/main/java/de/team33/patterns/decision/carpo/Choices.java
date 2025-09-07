package de.team33.patterns.decision.carpo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A tool for collecting pairings of specific cases with an associated choice
 * and finally create a {@link Function} that maps an input of type &lt;I&gt; to a result of type &lt;R&gt;.
 *
 * @param <I> the intended input type.
 * @param <R> the intended result type.
 * @see de.team33.patterns.decision.carpo package
 */
public final class Choices<I, R> {

    private final Variety<? super I> variety;
    private final List<Function<I, R>> methods;

    private Choices(final Variety<? super I> variety) {
        //noinspection ReturnOfNull
        final Supplier<Function<I, R>> noMethod = () -> null;
        this.variety = variety;
        this.methods = Stream.generate(noMethod)
                             .limit(variety.bound())
                             .collect(Collectors.toCollection(ArrayList::new));
    }

    static <I> Start<I> start(final Variety<? super I> variety) {
        return bitSets -> new Cases.Start<>() {
            @Override
            public <R> Choices<I, R> apply(final Function<I, R> function) {
                return new Choices<I, R>(variety).on(bitSets)
                                                 .apply(function);
            }
        };
    }

    private static <I, R> Function<I, R> toFunction(final Variety<? super I> variety,
                                                    final List<? extends Function<I, R>> methods) {
        return input -> {
            final int bitSet = variety.apply(input);
            return Optional.ofNullable(methods.get(bitSet))
                           .orElseThrow(() -> new UnsupportedOperationException(
                                   String.format("Case %s is not supported", Integer.toBinaryString(bitSet))))
                           .apply(input);
        };
    }

    /**
     * Captures {@link Cases} that should be paired with a single specific choice.
     * A single case is represented by a unique <em>bitSet</em> (<em>int</em> value).
     */
    public Cases<I, R> on(final int... bitSets) {
        return method -> {
            for (final int bitSet : bitSets) {
                methods.set(bitSet, method);
            }
            return this;
        };
    }

    /**
     * Returns a {@link Function} generated from the collected pairings of cases/choices
     * that maps an input of type &lt;I&gt; to a result of type &lt;R&gt;.
     */
    public final Function<I, R> toFunction() {
        //noinspection Java9CollectionFactory
        return toFunction(variety, Collections.unmodifiableList(new ArrayList<>(methods)));
    }

    /**
     * A tool to start collecting pairings of specific cases with an associated choice
     * and finally create a {@link Function} that maps an input of type &lt;I&gt; to a result of a specific type.
     *
     * @param <I> the intended input type.
     */
    @FunctionalInterface
    interface Start<I> {

        /**
         * Captures a {@link Cases.Start first set of cases} that should be paired with a single specific choice.
         * A single case is represented by a unique <em>bitSet</em> (<em>int</em> value).
         */
        Cases.Start<I> on(int... bitSets);
    }
}
