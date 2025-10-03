package de.team33.patterns.streamable.galatea;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Represents instances that (virtually or really) contain elements of a specific type
 * and can provide a {@link Stream} over those elements when needed.
 *
 * @param <E> The type of contained elements.
 * @see #stream()
 */
@FunctionalInterface
public interface Streamable<E> {

    /**
     * Returns a {@link Streamable} that {@linkplain #isEmpty() is empty}.
     *
     * @param <E> The type of virtually contained elements.
     */
    static <E> Streamable<E> empty() {
        return Stream::empty;
    }

    /**
     * Returns a {@link Streamable} backed by a given {@link Iterable}.
     *
     * @param <E> The type of the contained elements.
     */
    static <E> Streamable<E> of(final Iterable<E> iterable) {
        if (iterable instanceof final Collection<E> collection) {
            return collection::stream;
        } else {
            return () -> StreamSupport.stream(iterable.spliterator(), false);
        }
    }

    /**
     * Returns a sequential {@code Stream} with <em>this</em> {@link Streamable} as its source.
     * <p>
     * An implementation may or may not specify a streaming order.
     */
    Stream<E> stream();

    /**
     * Returns {@code true} if <em>this</em> {@link Streamable} contains no element.
     */
    default boolean isEmpty() {
        return stream().findAny().isEmpty();
    }

    /**
     * Returns {@code true} if <em>this</em> {@link Streamable} contains any element.
     */
    default boolean containsAny() {
        return stream().findAny().isPresent();
    }

    /**
     * Returns {@code true} if <em>this</em> {@link Streamable} contains at least one element
     * such that <em>condition</em>{@link Predicate#test(Object) .test(element)} is {@code true}.
     *
     * @throws NullPointerException if the specified <em>condition</em> is {@code null}.
     */
    default boolean containsAny(final Predicate<? super E> condition) {
        return stream().anyMatch(condition);
    }

    /**
     * Returns {@code true} if <em>this</em> {@link Streamable} contains elements such that
     * <em>condition</em>{@link Predicate#test(Object) .test(element)} is {@code true} for each element.
     *
     * @throws NullPointerException if the specified <em>condition</em> is {@code null}.
     */
    default boolean containsAll(final Predicate<? super E> condition) {
        return stream().allMatch(condition);
    }

    /**
     * Returns {@code true} if <em>this</em> {@link Streamable} contains at least one <em>element</em>
     * such that {@code Objects.equals(element, candidate)}.
     */
    default boolean contains(final Object candidate) {
        return containsAny(element -> Objects.equals(element, candidate));
    }

    /**
     * Returns {@code true} if <em>this</em> {@link Streamable} contains any element of the specified
     * <em>other</em> {@link Streamable}.
     *
     * @throws NullPointerException if the specified <em>other</em> {@link Streamable} is {@code null}.
     */
    default <X> boolean containsAny(final Streamable<X> other) {
        return other.containsAny(this::contains);
    }

    /**
     * Returns {@code true} if <em>this</em> {@link Streamable} contains all elements of the specified
     * <em>other</em> {@link Streamable}.
     *
     * @throws NullPointerException if the specified <em>other</em> {@link Streamable} is {@code null}.
     */
    default <X> boolean containsAll(final Streamable<X> other) {
        return other.containsAll(this::contains);
    }

    /**
     * Performs the given <em>action</em> for each element of <em>this</em> {@link Streamable}
     * until all elements have been processed or the <em>action</em> throws an (unchecked) exception.
     * Actions are performed in the streaming order, if that order is specified.
     * Exceptions thrown by the <em>action</em> are relayed to the caller.
     *
     * @throws NullPointerException if the specified <em>action</em> is {@code null}.
     */
    default void forEach(final Consumer<? super E> action) {
        stream().forEach(action);
    }
}
