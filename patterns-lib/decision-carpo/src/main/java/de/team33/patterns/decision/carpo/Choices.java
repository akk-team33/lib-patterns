package de.team33.patterns.decision.carpo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Choices<I, R> {

    private final Variety<I> variety;
    private final List<Function<I, R>> methods;

    private Choices(final Variety<I> variety) {
        final Supplier<Function<I, R>> noMethod = () -> null;
        this.variety = variety;
        this.methods = Stream.generate(noMethod)
                             .limit(variety.bound())
                             .collect(Collectors.toCollection(ArrayList::new));
    }

    static <I> Start<I> start(final Variety<I> variety) {
        return bitSets -> new Cases.Start<I>() {
            @Override
            public <R> Choices<I, R> apply(final Function<I, R> function) {
                return new Choices<I, R>(variety).on(bitSets)
                                                 .apply(function);
            }
        };
    }

    private static <I, R> Function<I, R> toFunction(final Variety<I> variety,
                                                    final List<Function<I, R>> methods) {
        return input -> {
            final int bitSet = variety.apply(input);
            return Optional.ofNullable(methods.get(bitSet))
                           .map(method -> method.apply(input))
                           .orElseThrow(() -> new UnsupportedOperationException(
                                   String.format("Case %s is not supported", Integer.toBinaryString(bitSet))));
        };
    }

    public Cases<I, R> on(final int... bitSets) {
        return method -> {
            for (final int bitSet : bitSets) {
                methods.set(bitSet, method);
            }
            return this;
        };
    }

    public final Function<I, R> toFunction() {
        return toFunction(variety, Collections.unmodifiableList(new ArrayList<>(methods)));
    }

    public interface Start<I> {
        Cases.Start<I> on(int... bitSets);
    }
}
