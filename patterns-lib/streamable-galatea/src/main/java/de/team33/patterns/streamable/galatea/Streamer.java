package de.team33.patterns.streamable.galatea;

import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;

/**
 * @see <a href="https://www.team33.de/dev/patterns/2.x/patterns-lib/streamable-naiad/">streamable-naiad</a>
 * @see <a href="https://www.team33.de/dev/patterns/2.x/patterns-lib/streamable-naiad/apidocs/">streamable-naiad/apidocs</a>
 * @deprecated consider interface Streamable or class Streamer from module
 * <a href="https://www.team33.de/dev/patterns/2.x/patterns-lib/streamable-naiad/apidocs/">streamable-naiad</a>
 * as a replacement.
 */
@Deprecated
@FunctionalInterface
public interface Streamer<E> extends Streamable<E> {

    /**
     * @deprecated see {@link Streamer}.
     */
    @Deprecated
    static <E> Streamer<E> empty() {
        return Stream::empty;
    }

    /**
     * @deprecated see {@link Streamer}.
     */
    @Deprecated
    static <E> Streamer<E> of(final E element) {
        return by(Streamable.of(element));
    }

    /**
     * @deprecated see {@link Streamer}.
     */
    @Deprecated
    @SafeVarargs
    static <E> Streamer<E> of(final E element0, final E element1, final E... more) {
        return by(Streamable.of(element0, element1, more));
    }

    /**
     * @deprecated see {@link Streamer}.
     */
    @Deprecated
    static <E> Streamer<E> of(final E[] elements) {
        return by(Streamable.of(elements));
    }

    /**
     * @deprecated see {@link Streamer}.
     */
    @Deprecated
    static <E> Streamer<E> of(final Iterable<E> iterable) {
        return by(Streamable.of(iterable));
    }

    /**
     * @deprecated see {@link Streamer}.
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
     * @deprecated see {@link Streamer}.
     */
    @Deprecated
    static <E, F extends E, G extends E> Streamer<E> concat(final Streamable<F> left, final Streamable<G> right) {
        return () -> Stream.concat(left.stream(), right.stream());
    }

    /**
     * @deprecated see {@link Streamer}.
     */
    @Deprecated
    default Streamer<E> add(final E element) {
        return addAll(of(element));
    }

    /**
     * @deprecated see {@link Streamer}.
     */
    @Deprecated
    default <X extends E> Streamer<E> addAll(final Streamable<X> other) {
        return concat(this, other);
    }

    /**
     * @deprecated see {@link Streamer}.
     */
    @Deprecated
    default Streamer<E> remove(final Object candidate) {
        return removeAll(of(candidate));
    }

    /**
     * @deprecated see {@link Streamer}.
     */
    @Deprecated
    default <X> Streamer<E> removeAll(final Streamable<X> other) {
        return removeIf(other::contains);
    }

    /**
     * @deprecated see {@link Streamer}.
     */
    @Deprecated
    default Streamer<E> removeIf(final Predicate<? super E> condition) {
        return retainIf(not(condition));
    }

    /**
     * @deprecated see {@link Streamer}.
     */
    @Deprecated
    default <X> Streamer<E> retainAll(final Streamable<X> other) {
        return retainIf(other::contains);
    }

    /**
     * @deprecated see {@link Streamer}.
     */
    @Deprecated
    default Streamer<E> retainIf(final Predicate<? super E> condition) {
        return () -> stream().filter(condition);
    }
}
