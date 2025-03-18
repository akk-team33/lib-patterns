package de.team33.patterns.exceptional.dione;

final class Mutual {

    private Mutual() {}

    static <X extends Exception> XBiFunction<Void, Void, Void, X>
    normalized(final XRunnable<? extends X> xRunnable) {
        return (t, u) -> {
            xRunnable.run();
            return null;
        };
    }

    static <T, X extends Exception> XBiFunction<T, Void, Void, X>
    normalized(final XConsumer<? super T, ? extends X> xConsumer) {
        return (t, u) -> {
            xConsumer.accept(t);
            return null;
        };
    }

    static <T, U, X extends Exception> XBiFunction<T, U, Void, X>
    normalized(final XBiConsumer<? super T, ? super U, ? extends X> xBiConsumer) {
        return (t, u) -> {
            xBiConsumer.accept(t, u);
            return null;
        };
    }

    static <R, X extends Exception> XBiFunction<Void, Void, R, X>
    normalized(final XSupplier<? extends R, ? extends X> xSupplier) {
        return (t, u) -> xSupplier.get();
    }

    @SuppressWarnings("LambdaUnfriendlyMethodOverload")
    static <T, X extends Exception> XBiFunction<T, Void, Boolean, X>
    normalized(final XPredicate<? super T, ? extends X> xPredicate) {
        return (t, u) -> xPredicate.test(t);
    }

    static <T, U, X extends Exception> XBiFunction<T, U, Boolean, X>
    normalized(final XBiPredicate<? super T, ? super U, ? extends X> xBiPredicate) {
        return xBiPredicate::test;
    }

    @SuppressWarnings("LambdaUnfriendlyMethodOverload")
    static <T, R, X extends Exception> XBiFunction<T, Void, R, X>
    normalized(final XFunction<? super T, ? extends R, ? extends X> xFunction) {
        return (t, u) -> xFunction.apply(t);
    }
}
