package de.team33.patterns.streamable.galatea;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * @see <a href="https://www.team33.de/dev/patterns/2.x/patterns-lib/streamable-naiad/">streamable-naiad</a>
 * @see <a href="https://www.team33.de/dev/patterns/2.x/patterns-lib/streamable-naiad/apidocs/">streamable-naiad/apidocs</a>
 * @deprecated consider interface Streamable from module
 * <a href="https://www.team33.de/dev/patterns/2.x/patterns-lib/streamable-naiad/apidocs/">streamable-naiad</a>
 * as a replacement.
 */
@Deprecated
@FunctionalInterface
public interface Streamable<E> {

    /**
     * @deprecated see {@link Streamable}.
     */
    @Deprecated
    static <E> Streamable<E> empty() {
        return Stream::empty;
    }

    /**
     * @deprecated see {@link Streamable}.
     */
    @Deprecated
    static <E> Streamable<E> of(final E element) {
        return () -> Stream.of(element);
    }

    /**
     * @deprecated see {@link Streamable}.
     */
    @Deprecated
    @SafeVarargs
    static <E> Streamable<E> of(final E element0, final E element1, final E... more) {
        return () -> Stream.concat(Stream.of(element0, element1), Stream.of(more));
    }

    /**
     * @deprecated see {@link Streamable}.
     */
    @Deprecated
    static <E> Streamable<E> of(final E[] elements) {
        return () -> Stream.of(elements);
    }

    /**
     * @deprecated see {@link Streamable}.
     */
    @Deprecated
    static <E> Streamable<E> of(final Iterable<E> iterable) {
        if (iterable instanceof final Collection<E> collection) {
            return collection::stream;
        } else {
            return () -> StreamSupport.stream(iterable.spliterator(), false);
        }
    }

    /**
     * @deprecated see {@link Streamable}.
     */
    @Deprecated
    Stream<E> stream();

    /**
     * @deprecated see {@link Streamable}.
     */
    @Deprecated
    default boolean isEmpty() {
        return stream().findAny().isEmpty();
    }

    /**
     * @deprecated see {@link Streamable}.
     */
    @Deprecated
    default boolean containsAny() {
        return stream().findAny().isPresent();
    }

    /**
     * @deprecated see {@link Streamable}.
     */
    @Deprecated
    default boolean containsAny(final Predicate<? super E> condition) {
        return stream().anyMatch(condition);
    }

    /**
     * @deprecated see {@link Streamable}.
     */
    @Deprecated
    default boolean containsAll(final Predicate<? super E> condition) {
        return stream().allMatch(condition);
    }

    /**
     * @deprecated see {@link Streamable}.
     */
    @Deprecated
    default boolean contains(final Object candidate) {
        return containsAny(element -> Objects.equals(element, candidate));
    }

    /**
     * @deprecated see {@link Streamable}.
     */
    @Deprecated
    default <X> boolean containsAny(final Streamable<X> other) {
        return other.containsAny(this::contains);
    }

    /**
     * @deprecated see {@link Streamable}.
     */
    @Deprecated
    default <X> boolean containsAll(final Streamable<X> other) {
        return other.containsAll(this::contains);
    }

    /**
     * @deprecated see {@link Streamable}.
     */
    @Deprecated
    default void forEach(final Consumer<? super E> action) {
        stream().forEach(action);
    }
}
