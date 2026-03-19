package de.team33.patterns.hierarchy.mab;

import java.util.function.Supplier;

import static java.lang.System.Logger.Level.DEBUG;
import static java.lang.System.Logger.Level.WARNING;

final class Util {

    private static final System.Logger LOGGER = System.getLogger(Util.class.getCanonicalName());

    private Util() {
    }

    static void log(final Problem<?> problem) {
        final Supplier<String> msgSupplier = () -> "Cannot list node <%s>".formatted(problem.node());
        LOGGER.log(WARNING, msgSupplier);
        LOGGER.log(DEBUG, msgSupplier, problem.cause());
    }
}
