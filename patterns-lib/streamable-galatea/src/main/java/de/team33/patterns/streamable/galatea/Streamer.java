package de.team33.patterns.streamable.galatea;

import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;

/**
 * Represents a {@link Streamable} type that provides a kind of builder pattern.
 *
 * @param <E> The type of contained elements.
 */
@FunctionalInterface
public interface Streamer<E> extends Streamable<E> {

    /**
     * Returns a {@link Streamer} that {@linkplain #isEmpty() is empty}.
     *
     * @param <E> The type of virtually contained elements.
     */
    static <E> Streamer<E> empty() {
        return Stream::empty;
    }

    /**
     * Returns a {@link Streamer} that contains a single given <em>element</em>.
     *
     * @param <E> The type of the contained element.
     */
    static <E> Streamer<E> of(final E element) {
        return by(Streamable.of(element));
    }

    /**
     * Returns a {@link Streamer} that contains two or more given <em>elements</em>.
     *
     * @param <E> The type of the contained elements.
     */
    @SafeVarargs
    static <E> Streamer<E> of(final E element0, final E element1, final E... more) {
        return by(Streamable.of(element0, element1, more));
    }

    /**
     * Returns a {@link Streamer} backed by an array of <em>elements</em>.
     *
     * @param <E> The type of the contained elements.
     */
    static <E> Streamer<E> of(final E[] elements) {
        return by(Streamable.of(elements));
    }

    /**
     * Returns a {@link Streamer} backed by a given {@link Iterable}.
     *
     * @param <E> The type of the contained elements.
     */
    static <E> Streamer<E> of(final Iterable<E> iterable) {
        return by(Streamable.of(iterable));
    }

    /**
     * Returns a {@link Streamer} backed by a given {@link Streamable}.
     *
     * @param <E> The type of the contained elements.
     */
    static <E> Streamer<E> by(final Streamable<E> streamable) {
        if (streamable instanceof final Streamer<E> streamer) {
            return streamer;
        } else {
            return streamable::stream;
        }
    }

    /**
     * Returns a concatenated {@link Streamer} whose elements are all the elements of the <em>left</em> argument
     * followed by all the elements of the <em>right</em> argument.
     * The result has a streaming order if both of the arguments have a streaming order.
     *
     * @param <E> The element type of the resulting {@link Streamer}.
     * @param <F> The element type of the <em>left</em> argument.
     * @param <G> The element type of the <em>right</em> argument.
     * @throws NullPointerException if one of the arguments is {@code null}.
     */
    static <E, F extends E, G extends E> Streamer<E> concat(final Streamable<F> left, final Streamable<G> right) {
        return () -> Stream.concat(left.stream(), right.stream());
    }

    /**
     * Returns a concatenated {@link Streamer} whose elements are all the elements of <em>this</em>
     * followed by the given <em>element</em>.
     * The result has a streaming order if <em>this</em> has a streaming order.
     */
    default Streamer<E> add(final E element) {
        return addAll(of(element));
    }

    /**
     * Returns a concatenated {@link Streamer} whose elements are all the elements of <em>this</em>
     * followed by all the elements of the <em>other</em> {@link Streamer}.
     * The result has a streaming order if both, <em>this</em> and <em>other</em>, have a streaming order.
     *
     * @param <X> The element type of the <em>other</em> {@link Streamer}.
     * @throws NullPointerException if <em>other</em> is {@code null}.
     */
    default <X extends E> Streamer<E> addAll(final Streamable<X> other) {
        return concat(this, other);
    }

    /**
     * Returns a {@link Streamer} consisting of the elements of <em>this</em>
     * but not the given <em>candidate</em>.
     */
    default Streamer<E> remove(final Object candidate) {
        return removeAll(of(candidate));
    }

    /**
     * Returns a {@link Streamer} consisting of the elements of <em>this</em>
     * that are not contained in the <em>other</em> {@link Streamer}.
     *
     * @throws NullPointerException if the specified <em>other</em> {@link Streamer} is {@code null}.
     */
    default <X> Streamer<E> removeAll(final Streamable<X> other) {
        return removeIf(other::contains);
    }

    /**
     * Returns a {@link Streamer} consisting of the elements of <em>this</em> for which
     * <em>condition</em>{@link Predicate#test(Object) .test(element)} is {@code false}.
     *
     * @throws NullPointerException if the specified <em>condition</em> is {@code null}.
     */
    default Streamer<E> removeIf(final Predicate<? super E> condition) {
        return retainIf(not(condition));
    }

    /**
     * Returns a {@link Streamer} consisting of the elements of <em>this</em>
     * that are contained in the <em>other</em> {@link Streamer}.
     *
     * @throws NullPointerException if the specified <em>other</em> {@link Streamer} is {@code null}.
     */
    default <X> Streamer<E> retainAll(final Streamable<X> other) {
        return retainIf(other::contains);
    }

    /**
     * Returns a {@link Streamer} consisting of the elements of <em>this</em> for which
     * <em>condition</em>{@link Predicate#test(Object) .test(element)} is {@code true}.
     *
     * @throws NullPointerException if the specified <em>condition</em> is {@code null}.
     */
    default Streamer<E> retainIf(final Predicate<? super E> condition) {
        return () -> stream().filter(condition);
    }
}
