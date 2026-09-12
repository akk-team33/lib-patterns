package de.team33.patterns.streamable.naiad;

import de.team33.patterns.streamable.galatea.Buffer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;

/**
 * @deprecated use {@link Buffer} instead.
 */
@SuppressWarnings({"deprecation", "TypeMayBeWeakened"})
@Deprecated
public final class Streamer<E> implements Streamable<E> {

    private final List<E> backing;

    private Streamer(final Streamable<E> source) {
        this.backing = source.stream()
                             .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * @deprecated use {@link Buffer#empty()} instead.
     */
    @Deprecated
    public static <E> Streamer<E> empty() {
        return by(Streamable.empty());
    }

    /**
     * @deprecated use {@link Buffer#of(Object)} instead.
     */
    @Deprecated
    public static <E> Streamer<E> of(final E element) {
        return by(Streamable.of(element));
    }

    /**
     * @deprecated use {@link Buffer#of(Object, Object, Object[])} instead.
     */
    @Deprecated
    @SafeVarargs
    public static <E> Streamer<E> of(final E element0, final E element1, final E... more) {
        return by(Streamable.of(element0, element1, more));
    }

    /**
     * @deprecated use {@link Buffer#of(Object[])} instead.
     */
    @Deprecated
    public static <E> Streamer<E> of(final E[] elements) {
        return by(Streamable.of(elements));
    }

    /**
     * @deprecated use {@link Buffer#of(Iterable)} instead.
     */
    @Deprecated
    public static <E> Streamer<E> of(final Iterable<E> iterable) {
        return by(Streamable.of(iterable));
    }

    /**
     * @deprecated use {@link Buffer#by(de.team33.patterns.streamable.galatea.Streamable)} instead.
     */
    @Deprecated
    public static <E> Streamer<E> by(final Streamable<E> streamable) {
        return new Streamer<>(streamable);
    }

    private Streamer<E> setup(final Consumer<? super List<E>> consumer) {
        consumer.accept(backing);
        return this;
    }

    /**
     * @deprecated use {@link Buffer#stream()} instead.
     */
    @Deprecated
    @Override
    public final Stream<E> stream() {
        return backing.stream();
    }

    /**
     * @deprecated use {@link Buffer#add(Object)} instead.
     */
    @Deprecated
    @Override
    public final Streamer<E> add(final E element) {
        return setup(list -> list.add(element));
    }

    /**
     * @deprecated use {@link Buffer#addAll(de.team33.patterns.streamable.galatea.Streamable)} instead.
     */
    @Deprecated
    @Override
    public final <X extends E> Streamer<E> addAll(final Streamable<X> other) {
        return setup(list -> list.addAll(other.toList()));
    }

    /**
     * @deprecated use {@link Buffer#remove(Object)} instead.
     */
    @Deprecated
    @Override
    public final Streamer<E> remove(final Object candidate) {
        return removeIf(element -> Objects.equals(element, candidate));
    }

    /**
     * @deprecated use {@link Buffer#removeAll(de.team33.patterns.streamable.galatea.Streamable)} instead.
     */
    @Deprecated
    @Override
    public final <X> Streamer<E> removeAll(final Streamable<X> other) {
        return removeIf(other::contains);
    }

    /**
     * @deprecated use {@link Buffer#removeIf(Predicate)} instead.
     */
    @Deprecated
    @Override
    public final Streamer<E> removeIf(final Predicate<? super E> condition) {
        return setup(list -> list.removeIf(condition));
    }

    /**
     * @deprecated use {@link Buffer#retainAll(de.team33.patterns.streamable.galatea.Streamable)} instead.
     */
    @Deprecated
    @Override
    public final <X> Streamer<E> retainAll(final Streamable<X> other) {
        return retainIf(other::contains);
    }

    /**
     * @deprecated use {@link Buffer#retainIf(Predicate)} instead.
     */
    @Deprecated
    @Override
    public final Streamer<E> retainIf(final Predicate<? super E> condition) {
        return setup(list -> list.removeIf(not(condition)));
    }
}
