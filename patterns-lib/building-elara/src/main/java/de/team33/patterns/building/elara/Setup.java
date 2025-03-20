package de.team33.patterns.building.elara;

import java.util.function.Consumer;

/**
 * Defines a generic interface for a uniform, basic implementation of a typical builder pattern.
 * It assumes that the information to be gathered during the build process should be collected in a
 * target instance attached to or created by the builder.
 *
 * @param <C> The container type.
 * @param <B> The builder type: the effective type of the derived builder implementation,
 *            at least this type itself.
 */
@FunctionalInterface
public interface Setup<C, B> {

    /**
     * Accepts a {@link Consumer} as modifying operation to be performed on a target instance immediately
     * or no later than the final build() operation and returns the builder instance itself.
     */
    B setup(Consumer<? super C> consumer);
}
