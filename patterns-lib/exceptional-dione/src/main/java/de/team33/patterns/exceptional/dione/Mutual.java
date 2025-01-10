package de.team33.patterns.exceptional.dione;

final class Mutual {

    private Mutual() {}

    static <X extends Exception> XBiFunction<Void, Void, Void, X>
    normalized(final XRunnable<X> xRunnable) {
        return (t, u) -> {
            xRunnable.run();
            return null;
        };
    }

    static <T, X extends Exception> XBiFunction<T, Void, Void, X>
    normalized(final XConsumer<T, X> xConsumer) {
        return (t, u) -> {
            xConsumer.accept(t);
            return null;
        };
    }

    static <T, U, X extends Exception> XBiFunction<T, U, Void, X>
    normalized(final XBiConsumer<T, U, X> xBiConsumer) {
        return (t, u) -> {
            xBiConsumer.accept(t, u);
            return null;
        };
    }

    static <R, X extends Exception> XBiFunction<Void, Void, R, X>
    normalized(final XSupplier<R, X> xSupplier) {
        return (t, u) -> xSupplier.get();
    }

    static <T, X extends Exception> XBiFunction<T, Void, Boolean, X>
    normalized(final XPredicate<T, X> xPredicate) {
        return (t, u) -> xPredicate.test(t);
    }

    static <T, U, X extends Exception> XBiFunction<T, U, Boolean, X>
    normalized(final XBiPredicate<T, U, X> xBiPredicate) {
        return xBiPredicate::test;
    }

    static <T, R, X extends Exception> XBiFunction<T, Void, R, X>
    normalized(final XFunction<T, R, X> xFunction) {
        return (t, u) -> xFunction.apply(t);
    }
}
