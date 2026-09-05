package de.team33.patterns.building.elara;

import de.team33.patterns.streamable.galatea.Streamable;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Defines a generic interface for a uniform, basic implementation of a typical builder pattern.
 * It assumes that the information to be gathered during the build process should be collected in a
 * target container instance attached to or created by the builder.
 *
 * @param <C> The underlying target container type.
 * @param <S> The setup type: the intended effective type of the concrete builder implementation.
 */
@FunctionalInterface
public interface Setup<C, S> {

    /**
     * Accepts a {@link Consumer} as modifying operation to be performed on a target container instance immediately
     * or no later than the final build() operation and returns the setup instance itself.
     */
    S setup(Consumer<? super C> consumer);

    /**
     * @deprecated use {@link #forEach(Stream)} instead.
     */
    @Deprecated
    default <A> S forEach(final Streamable<A> arguments, final BiFunction<? super S, ? super A, S> setupMethod) {
        return forEach(arguments.stream()).apply(setupMethod);
    }

    /**
     * Returns a {@link Function} that performs a given <em>method</em> for each <em>argument</em>
     * provided by the given {@link Stream} on <em>this</em> and that finally returns <em>this</em>.
     *
     * @param <A> The <em>argument</em> type.
     */
    default <A> Function<BiFunction<? super S, ? super A, S>, S> forEach(final Stream<? extends A> arguments) {
        return method -> arguments.map(arg -> Util.function(method, arg))
                                  .reduce(Function::andThen)
                                  .orElse(Function.identity())
                                  .apply(setup(Util.NOP));
    }
}
