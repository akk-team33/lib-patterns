package de.team33.patterns.building.elara;

import java.util.function.Consumer;

/**
 * Utility class for this package.
 */
final class Building {

    private static final String ILLEGAL_BUILDER_CLASS =
            "<left> is expected to represent <right> (%s) - but was %s";

    private Building() {
    }

    /**
     * Ensures that the class or interface represented by the <em>"left"</em> {@link Class} parameter is
     * either the same as, or a superclass or superinterface of, the class or interface represented by the
     * <em>"right"</em> {@link Class} parameter.
     *
     * @throws IllegalArgumentException If the situation described does not apply.
     */
    static void ensureAssignable(final Class<?> left, final Class<?> right) {
        if (!left.isAssignableFrom(right)) {
            throw new IllegalArgumentException(String.format(ILLEGAL_BUILDER_CLASS, right, left));
        }
    }
}
