package de.team33.patterns.building.elara;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

final class Util {

    static final Consumer<Object> NOP = any -> {
    };

    private Util() {
    }

    static <A, B> Function<B, B> function(final BiFunction<? super B, ? super A, B> method, final A arg) {
        return b -> method.apply(b, arg);
    }
}
