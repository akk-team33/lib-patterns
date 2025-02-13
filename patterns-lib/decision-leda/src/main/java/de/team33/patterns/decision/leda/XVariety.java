package de.team33.patterns.decision.leda;

import java.util.function.Function;
import java.util.function.Predicate;

public class XVariety<I, R> {

    @SafeVarargs
    public static <I> ProChoice<I> joining(final Predicate<? super I>... conditions) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    public final R apply(final I input) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    public static class ProChoice<I> {
        public ProCase<I> on(final int ... bitPatterns) {
            throw new UnsupportedOperationException("not yet implemented");
        }
    }

    public static class ProCase<I> {
        public <R> Choice<I, R> reply(final R result) {
            throw new UnsupportedOperationException("not yet implemented");
        }

        public <R> Choice<I, R> apply(final Function<I, R> function) {
            throw new UnsupportedOperationException("not yet implemented");
        }
    }

    public static class Choice<I, R> {
        public Case<I, R> on(final int ... bitPatterns) {
            throw new UnsupportedOperationException("not yet implemented");
        }

        public XVariety<I, R> build() {
            throw new UnsupportedOperationException("not yet implemented");
        }
    }

    public static class Case<I, R> {
        public Choice<I, R> reply(final R result) {
            throw new UnsupportedOperationException("not yet implemented");
        }

        public Choice<I, R> apply(final Function<I, R> function) {
            throw new UnsupportedOperationException("not yet implemented");
        }
    }
}
