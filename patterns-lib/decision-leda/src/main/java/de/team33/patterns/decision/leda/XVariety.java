package de.team33.patterns.decision.leda;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

public class XVariety<I, R> {

    private static final String ILLEGAL_ARGUMENTS =
            "For %d independent conditions, %d possible replies must be defined - but %d are given: %n%n    %s%n";

    private final IntVariety<I> backing;
    private final List<Function<I, R>> results;

    XVariety(final IntVariety<I> backing, final Collection<? extends Function<I, R>> results) {
        this.backing = backing;
        if (backing.bound() == results.size()) {
            this.results = Collections.unmodifiableList(new ArrayList<>(results));
        } else {
            throw new IllegalArgumentException(String.format(ILLEGAL_ARGUMENTS, backing.scale(), backing.bound(), results.size(), results));
        }
    }

    @SafeVarargs
    public static <I> Choices.Start<I> joining(final Predicate<I>... conditions) {
        return Choices.start(IntVariety.joined(conditions));
    }

    public final R apply(final I input) {
        final int bits = backing.apply(input);
        return Optional.ofNullable(results.get(bits))
                       .map(method -> method.apply(input))
                       .orElseThrow(() -> new UnsupportedOperationException(
                               String.format("Case %s not supported", Integer.toBinaryString(bits))));
    }

}
