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
     * Returns a concatenated {@link Streamable} whose elements are all the elements of the <em>left</em> argument
     * followed by all the elements of the <em>right</em> argument.
     * The result has a streaming order if both of the arguments have a streaming order.
     *
     * @param <E> The element type of the resulting {@link Streamable}.
     * @param <F> The element type of the <em>left</em> argument.
     * @param <G> The element type of the <em>right</em> argument.
     * @throws NullPointerException if one of the arguments is {@code null}.
     */
    static <E, F extends E, G extends E> Streamable<E> concat(final Streamable<F> left, final Streamable<G> right) {
        return () -> Stream.concat(left.stream(), right.stream());
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

    /**
     * Returns a concatenated {@link Streamable} whose elements are all the elements of <em>this</em>
     * followed by the given <em>element</em>.
     * The result has a streaming order if <em>this</em> has a streaming order.
     */
    default Streamable<E> add(final E element) {
        return addAll(() -> Stream.of(element));
    }

    /**
     * Returns a concatenated {@link Streamable} whose elements are all the elements of <em>this</em>
     * followed by all the given <em>elements</em>.
     * The result has a streaming order if <em>this</em> has a streaming order.
     */
    @SuppressWarnings("unchecked")
    default Streamable<E> add(final E element0, final E element1, final E... more) {
        return addAll(() -> Stream.concat(Stream.of(element0, element1), Stream.of(more)));
    }

    /**
     * Returns a concatenated {@link Streamable} whose elements are all the elements of <em>this</em>
     * followed by all the elements of the given <em>array</em>.
     * The result has a streaming order if <em>this</em> has a streaming order.
     *
     * @throws NullPointerException if <em>array</em> is {@code null}.
     */
    default Streamable<E> addAll(final E[] array) {
        return addAll(() -> Stream.of(array));
    }

    /**
     * Returns a concatenated {@link Streamable} whose elements are all the elements of <em>this</em>
     * followed by all the elements of the <em>other</em> {@link Streamable}.
     * The result has a streaming order if both, <em>this</em> and <em>other</em>, have a streaming order.
     *
     * @param <F> The element type of the <em>other</em> {@link Streamable}.
     * @throws NullPointerException if <em>other</em> is {@code null}.
     */
    default <F extends E> Streamable<E> addAll(final Streamable<F> other) {
        return concat(this, other);
    }
}
