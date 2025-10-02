package de.team33.patterns.streamable.galatea;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

@FunctionalInterface
public interface Streamable<E> {

    static <E> Streamable<E> empty() {
        return Stream::empty;
    }

    static <E> Streamable<E> of(final E element) {
        return () -> Stream.of(element);
    }

    @SafeVarargs
    static <E> Streamable<E> of(final E element0, final E element1, final E ... more) {
        return () -> Stream.concat(Stream.of(element0, element1), Stream.of(more));
    }

    /**
     * Returns a sequential {@code Stream} with <em>this</em> streamable as its source.
     * <p>
     * An implementation may or may not specify a streaming order.
     */
    Stream<E> stream();

    /**
     * Returns {@code true} if <em>this</em> streamable contains at least one <em>element</em>
     * such that <em>condition</em>{@link Predicate#test(Object) .test(element)} is {@code true}.
     */
    default boolean containsAny(final Predicate<? super E> condition) {
        return stream().anyMatch(condition);
    }

    /**
     * Returns {@code true} if <em>this</em> streamable contains at least one <em>element</em>
     * such that {@code Objects.equals(element, candidate)}.
     */
    default boolean contains(final Object candidate) {
        return containsAny(element -> Objects.equals(element, candidate));
    }

    /**
     * Returns {@code true} if <em>this</em> streamable contains all the specified <em>candidates</em>.
     */
    default boolean containsAll(final Streamable<?> candidates) {
        return candidates.stream().allMatch(this::contains);
    }

    /**
     * Performs the given <em>action</em> for each element of <em>this</em> streamable
     * until all elements have been processed or the action throws an exception.
     * Actions are performed in the streaming order, if that order is specified.
     * Exceptions thrown by the action are relayed to the caller.
     *
     * @throws NullPointerException if the specified <em>action</em> is {@code null}.
     */
    default void forEach(final Consumer<? super E> action) {
        stream().forEach(action);
    }
}
