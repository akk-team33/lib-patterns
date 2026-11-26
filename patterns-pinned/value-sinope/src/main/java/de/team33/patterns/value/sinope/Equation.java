package de.team33.patterns.value.sinope;

import java.util.function.Function;

public final class Equation<T> {

    private final Class<T> type;
    private final Function<T, ?> function;

    private Equation(final Class<T> type, final Function<T, ?> function) {
        this.type = type;
        this.function = function;
    }

    public static <T> Equation<T> of(final Class<T> type, final Function<T, ?> function) {
        return new Equation<>(type, function);
    }

    public final boolean equals(final T left, final Object right) {
        return left == right || (type.isInstance(right) && function.apply(left)
                                                                   .equals(function.apply(type.cast(right))));
    }

    public final int hashCode(final T instance) {
        return function.apply(instance).hashCode();
    }

    public final String toString(final T instance) {
        return function.apply(instance).toString();
    }
}
