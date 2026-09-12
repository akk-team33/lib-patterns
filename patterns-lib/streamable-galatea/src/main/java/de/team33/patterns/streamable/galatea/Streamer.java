package de.team33.patterns.streamable.galatea;

import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;

/**
 * @deprecated This implementation is substantially inefficient. Use {@link Buffer} instead.
 */
@Deprecated
@FunctionalInterface
public interface Streamer<E> extends Streamable<E> {

    /**
     * @deprecated use {@link Buffer#empty()} instead.
     */
    @Deprecated
    static <E> Streamer<E> empty() {
        return Stream::empty;
    }

    /**
     * @deprecated use {@link Buffer#of(Object)} instead.
     */
    @Deprecated
    static <E> Streamer<E> of(final E element) {
        return by(Streamable.of(element));
    }

    /**
     * @deprecated use {@link Buffer#of(Object, Object, Object[])} instead.
     */
    @Deprecated
    @SafeVarargs
    static <E> Streamer<E> of(final E element0, final E element1, final E... more) {
        return by(Streamable.of(element0, element1, more));
    }

    /**
     * @deprecated use {@link Buffer#of(Object[])} instead.
     */
    @Deprecated
    static <E> Streamer<E> of(final E[] elements) {
        return by(Streamable.of(elements));
    }

    /**
     * @deprecated use {@link Buffer#of(Iterable)} instead.
     */
    @Deprecated
    static <E> Streamer<E> of(final Iterable<E> iterable) {
        return by(Streamable.of(iterable));
    }

    /**
     * @deprecated use {@link Buffer#by(Streamable)} instead.
     */
    @Deprecated
    static <E> Streamer<E> by(final Streamable<E> streamable) {
        if (streamable instanceof final Streamer<E> streamer) {
            return streamer;
        } else {
            return streamable::stream;
        }
    }

    /**
     * @deprecated see {@link Streamer}
     */
    @Deprecated
    static <E, F extends E, G extends E> Streamer<E> concat(final Streamable<F> left, final Streamable<G> right) {
        return () -> Stream.concat(left.stream(), right.stream());
    }

    /**
     * @deprecated use {@link Buffer#add(Object)} instead.
     */
    @Deprecated
    default Streamer<E> add(final E element) {
        return addAll(of(element));
    }

    /**
     * @deprecated use {@link Buffer#addAll(Streamable)} instead.
     */
    @Deprecated
    default <X extends E> Streamer<E> addAll(final Streamable<X> other) {
        return concat(this, other);
    }

    /**
     * @deprecated use {@link Buffer#remove(Object)} instead.
     */
    @Deprecated
    default Streamer<E> remove(final Object candidate) {
        return removeAll(of(candidate));
    }

    /**
     * @deprecated use {@link Buffer#removeAll(Streamable)} instead.
     */
    @Deprecated
    default <X> Streamer<E> removeAll(final Streamable<X> other) {
        return removeIf(other::contains);
    }

    /**
     * @deprecated use {@link Buffer#removeIf(Predicate)} instead.
     */
    @Deprecated
    default Streamer<E> removeIf(final Predicate<? super E> condition) {
        return retainIf(not(condition));
    }

    /**
     * @deprecated use {@link Buffer#retainAll(Streamable)} instead.
     */
    @Deprecated
    default <X> Streamer<E> retainAll(final Streamable<X> other) {
        return retainIf(other::contains);
    }

    /**
     * @deprecated use {@link Buffer#retainIf(Predicate)} instead.
     */
    @Deprecated
    default Streamer<E> retainIf(final Predicate<? super E> condition) {
        return () -> stream().filter(condition);
    }
}
