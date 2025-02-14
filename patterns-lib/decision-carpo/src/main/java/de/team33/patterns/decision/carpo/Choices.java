package de.team33.patterns.decision.carpo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Choices<I, R> {

    private final IntVariety<I> backing;
    private final List<Function<I, R>> methods;

    private Choices(final IntVariety<I> backing) {
        final Supplier<Function<I, R>> noMethod = () -> null;
        this.backing = backing;
        this.methods = Stream.generate(noMethod)
                             .limit(backing.bound())
                             .collect(Collectors.toCollection(ArrayList::new));
    }

    public static <I> Start<I> start(final IntVariety<I> variety) {
        return bitSets -> new Cases.First<I>() {
            @Override
            public <R> Choices<I, R> apply(final Function<I, R> function) {
                return new Choices<I, R>(variety).on(bitSets)
                                                 .apply(function);
            }
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

    public final Variety<I, R> variety() {
        return new Variety<>(backing, methods);
    }

    public interface Start<I> {
        Cases.First<I> on(int... bitSets);
    }
}
