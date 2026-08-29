package de.team33.patterns.streamable.naiad;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.function.Predicate.not;

/**
 * Represents a reusable source of elements of a specific type that provides
 * a sequential {@link Stream} over those elements.
 * <p>
 * Each invocation of {@link #stream()} provides a new stream over the elements
 * represented by this instance. If the elements have a defined streaming order,
 * that order is preserved.
 * <p>
 * NOTE that some methods may not terminate if an involved stream is infinite.
 *
 * @param <E> The type of contained elements.
 * @see #stream()
 */
@SuppressWarnings("ClassWithTooManyMethods")
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
     * Returns a {@link Streamable} that contains a single given <em>element</em>.
     *
     * @param <E> The type of the contained element.
     */
    static <E> Streamable<E> of(final E element) {
        return () -> Stream.of(element);
    }

    /**
     * Returns a {@link Streamable} that contains two or more given <em>elements</em>.
     *
     * @param <E> The type of the contained elements.
     */
    @SafeVarargs
    static <E> Streamable<E> of(final E element0, final E element1, final E... more) {
        return () -> Stream.concat(Stream.of(element0, element1), Stream.of(more));
    }

    /**
     * Returns a {@link Streamable} backed by an array of <em>elements</em>.
     *
     * @param <E> The type of the contained elements.
     */
    static <E> Streamable<E> of(final E[] elements) {
        return () -> Stream.of(elements);
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
     * Returns a sequential {@link Stream} with <em>this</em> {@link Streamable} as its source.
     * <p>
     * Each invocation returns a new stream over the elements represented by <em>this</em> instance.
     * If <em>this</em> {@link Streamable} has a streaming order, the returned stream preserves that order.
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
     * Returns {@code true} if <em>this</em> {@link Streamable} contains no element
     * for which <em>condition</em>{@link Predicate#test(Object) .test(element)} is {@code false}.
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
     * Returns a concatenated {@link Streamable} whose elements are all the elements of <em>this</em> instance
     * followed by the given <em>element</em>.
     * The result has a streaming order if <em>this</em> has a streaming order.
     * <p>
     * The default implementation returns a new {@link Streamable} and leaves <em>this</em> unaffected.
     * Repeated application of this method may result in deeply nested stream compositions
     * and may therefore be unsuitable for extensive use. Implementations may return <em>this</em> instead.
     */
    default Streamable<E> add(final E element) {
        return addAll(of(element));
    }

    /**
     * Returns a concatenated {@link Streamable} whose elements are all the elements of <em>this</em> instance
     * followed by all the elements of the <em>other</em> one.
     * The result has a streaming order if both <em>this</em> and <em>other</em> have a streaming order.
     * <p>
     * The default implementation returns a new {@link Streamable} and leaves <em>this</em> unaffected.
     * Repeated application of this method may result in deeply nested stream compositions
     * and may therefore be unsuitable for extensive use. Implementations may return <em>this</em> instead.
     *
     * @param <X> The element type of the <em>other</em> {@link Streamable}.
     * @throws NullPointerException if <em>other</em> is {@code null}.
     */
    default <X extends E> Streamable<E> addAll(final Streamable<X> other) {
        return () -> Stream.concat(this.stream(), other.stream());
    }

    /**
     * Returns a {@link Streamable} consisting of the elements of <em>this</em> instance
     * but not the given <em>candidate</em>.
     * The result has a streaming order if <em>this</em> has a streaming order.
     * <p>
     * The default implementation returns a new {@link Streamable} and leaves <em>this</em> unaffected.
     * Implementations may return <em>this</em> instead.
     */
    default Streamable<E> remove(final Object candidate) {
        return removeAll(of(candidate));
    }

    /**
     * Returns a {@link Streamable} consisting of the elements of <em>this</em> instance
     * that are not contained in the <em>other</em> one.
     * The result has a streaming order if <em>this</em> has a streaming order.
     * <p>
     * The default implementation returns a new {@link Streamable} and leaves <em>this</em> unaffected.
     * Implementations may return <em>this</em> instead.
     *
     * @throws NullPointerException if the specified <em>other</em> {@link Streamable} is {@code null}.
     */
    default <X> Streamable<E> removeAll(final Streamable<X> other) {
        return removeIf(other::contains);
    }

    /**
     * Returns a {@link Streamable} consisting of the elements of <em>this</em> instance for which
     * <em>condition</em>{@link Predicate#test(Object) .test(element)} is {@code false}.
     * The result has a streaming order if <em>this</em> has a streaming order.
     * <p>
     * The default implementation returns a new {@link Streamable} and leaves <em>this</em> unaffected.
     * Implementations may return <em>this</em> instead.
     *
     * @throws NullPointerException if the specified <em>condition</em> is {@code null}.
     */
    default Streamable<E> removeIf(final Predicate<? super E> condition) {
        return retainIf(not(condition));
    }

    /**
     * Returns a {@link Streamable} consisting of the elements of <em>this</em> instance
     * that are contained in the <em>other</em> one.
     * The result has a streaming order if <em>this</em> has a streaming order.
     * <p>
     * The default implementation returns a new {@link Streamable} and leaves <em>this</em> unaffected.
     * Implementations may return <em>this</em> instead.
     *
     * @throws NullPointerException if the specified <em>other</em> {@link Streamable} is {@code null}.
     */
    default <X> Streamable<E> retainAll(final Streamable<X> other) {
        return retainIf(other::contains);
    }

    /**
     * Returns a {@link Streamable} consisting of the elements of <em>this</em> instance for which
     * <em>condition</em>{@link Predicate#test(Object) .test(element)} is {@code true}.
     * The result has a streaming order if <em>this</em> has a streaming order.
     * <p>
     * The default implementation returns a new {@link Streamable} and leaves <em>this</em> unaffected.
     * Implementations may return <em>this</em> instead.
     *
     * @throws NullPointerException if the specified <em>condition</em> is {@code null}.
     */
    default Streamable<E> retainIf(final Predicate<? super E> condition) {
        return () -> stream().filter(condition);
    }

    /**
     * Returns an unmodifiable {@link List} containing all the elements of <em>this</em> instance,
     * preserving its streaming order, if one exists.
     * <p>
     * The returned list contains all elements represented by <em>this</em> at the time of invocation.
     *
     * @see Stream#toList()
     */
    default List<E> toList() {
        return stream().toList();
    }

    /**
     * Returns an unmodifiable {@link Set} containing all distinct elements of <em>this</em> instance.
     *
     * @throws NullPointerException if <em>this</em> contains {@code null}.
     * @see Stream#collect(Collector)
     * @see Collectors#toUnmodifiableSet()
     */
    default Set<E> toSet() {
        return stream().collect(Collectors.toUnmodifiableSet());
    }

    /**
     * Applies the given <em>method</em> to <em>this</em> {@link Streamable} and returns the result.
     * <p>
     * This method is useful for applying a function to a {@link Streamable} within a fluent expression.
     *
     * @param <T> The result type.
     * @throws NullPointerException if the specified <em>method</em> is {@code null}.
     */
    default <T> T map(final Function<? super Streamable<E>, T> method) {
        return method.apply(this);
    }
}
