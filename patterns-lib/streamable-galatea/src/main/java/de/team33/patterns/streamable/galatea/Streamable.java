package de.team33.patterns.streamable.galatea;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@FunctionalInterface
public interface Streamable<E> {

    static <E> Streamable<E> empty() {
        return Stream::empty;
    }

    static <E> Streamable<E> of(final E element) {
        return () -> Stream.of(element);
    }

    @SafeVarargs
    static <E> Streamable<E> of(final E element0, final E element1, final E... more) {
        return () -> Stream.concat(Stream.of(element0, element1), Stream.of(more));
    }

    static <E> Streamable<E> of(final E[] elements) {
        return () -> Stream.of(elements);
    }

    static <E> Streamable<E> of(final Iterable<E> iterable) {
        if (iterable instanceof final Collection<E> collection) {
            return collection::stream;
        } else {
            return () -> StreamSupport.stream(iterable.spliterator(), false);
        }
    }

    static <E, X> Streamable<X> map(final Streamable<E> origin,
                                    final Function<? super Stream<E>, ? extends Stream<X>> mapping) {
        return origin.map(mapping);
    }

    static <E extends X, X> Streamable<X> map(final Streamable<E> origin) {
        return origin.map(stream -> stream.map(e -> (X) e));
    }

    /**
     * Returns a sequential {@code Stream} with <em>this</em> streamable as its source.
     * <p>
     * An implementation may or may not specify a streaming order.
     */
    Stream<E> stream();

    /**
     * Returns {@code true} if <em>this</em> streamable contains no elements.
     */
    default boolean isEmpty() {
        return stream().findAny().isEmpty();
    }

    /**
     * Returns {@code true} if <em>this</em> aggregate contains any element.
     */
    default boolean containsAny() {
        return stream().findAny().isPresent();
    }

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
    default <X> boolean containsAny(final Streamable<X> candidates) {
        return candidates.stream().anyMatch(this::contains);
    }

    /**
     * Returns {@code true} if <em>this</em> streamable contains all the specified <em>candidates</em>.
     */
    default <X> boolean containsAll(final Streamable<X> candidates) {
        return candidates.stream().allMatch(this::contains);
    }

    default <X> Streamable<X> map(final Function<? super Stream<E>, ? extends Stream<X>> mapping) {
        return () -> mapping.apply(stream());
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
