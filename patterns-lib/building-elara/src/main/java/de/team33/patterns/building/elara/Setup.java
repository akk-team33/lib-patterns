package de.team33.patterns.building.elara;

import de.team33.patterns.streamable.galatea.Streamable;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Defines a generic interface for a uniform, basic implementation of a typical builder pattern.
 * It assumes that the information to be gathered during the build process should be collected in a
 * target instance attached to or created by the builder.
 *
 * @param <C> The container type.
 * @param <B> The builder type: the effective type of the derived builder implementation.
 */
@FunctionalInterface
public interface Setup<C, B> {

    /**
     * Accepts a {@link Consumer} as modifying operation to be performed on a target instance immediately
     * or no later than the final build() operation and returns the builder instance itself.
     */
    B setup(Consumer<? super C> consumer);

    default <A> B forEach(final Streamable<A> args, final BiFunction<? super B, ? super A, B> builderMethod) {
        return args.stream()
                   .map(arg -> Util.function(builderMethod, arg))
                   .reduce(Function::andThen)
                   .orElse(Function.identity())
                   .apply(setup(Util.NOP));
    }
}
