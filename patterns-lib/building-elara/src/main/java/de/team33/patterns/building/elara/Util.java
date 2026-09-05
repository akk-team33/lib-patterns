package de.team33.patterns.building.elara;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

final class Util {

    static final Consumer<Object> NOP = any -> {
    };

    private Util() {
    }

    static <A, S> Function<S, S> function(final BiFunction<? super S, ? super A, ? extends S> setupMethod,
                                          final A argument) {
        return setup -> setupMethod.apply(setup, argument);
    }
}
