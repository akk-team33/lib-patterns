package de.team33.patterns.decision.carpo;

import java.util.function.Function;

@FunctionalInterface
public interface Cases<I, R> {
    default Choices<I, R> reply(final R result) {
        return apply(any -> result);
    }

    Choices<I, R> apply(final Function<I, R> function);

    abstract class Start<I> {

        public final <R> Choices<I, R> reply(final R result) {
            return apply(any -> result);
        }

        public abstract <R> Choices<I, R> apply(final Function<I, R> function);
    }
}
