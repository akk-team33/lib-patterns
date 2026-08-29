package de.team33.patterns.streamable.naiad;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;

/**
 * A mutable {@link Streamable} implementation that provides a builder pattern.
 * <p>
 * Builder methods modify this instance and return <em>this</em> to allow chaining.
 *
 * @param <E> The type of contained elements.
 */
public final class Streamer<E> implements Streamable<E> {

    private final List<E> backing;

    private Streamer(final Streamable<E> source) {
        this.backing = source.stream()
                             .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Returns a {@link Streamer} that {@linkplain #isEmpty() is empty}.
     *
     * @param <E> The type of virtually contained elements.
     */
    public static <E> Streamer<E> empty() {
        return by(Streamable.empty());
    }

    /**
     * Returns a {@link Streamer} that contains a single given <em>element</em>.
     *
     * @param <E> The type of the contained element.
     */
    public static <E> Streamer<E> of(final E element) {
        return by(Streamable.of(element));
    }

    /**
     * Returns a {@link Streamer} that contains two or more given <em>elements</em>.
     *
     * @param <E> The type of the contained elements.
     */
    @SafeVarargs
    public static <E> Streamer<E> of(final E element0, final E element1, final E... more) {
        return by(Streamable.of(element0, element1, more));
    }

    /**
     * Returns a {@link Streamer} backed by an array of <em>elements</em>.
     *
     * @param <E> The type of the contained elements.
     */
    public static <E> Streamer<E> of(final E[] elements) {
        return by(Streamable.of(elements));
    }

    /**
     * Returns a {@link Streamer} backed by a given {@link Iterable}.
     *
     * @param <E> The type of the contained elements.
     */
    public static <E> Streamer<E> of(final Iterable<E> iterable) {
        return by(Streamable.of(iterable));
    }

    /**
     * Returns a {@link Streamer} initially containing all elements of the given <em>streamable</em>.
     * <p>
     * The elements are copied from <em>streamable</em>;
     * subsequent modifications of the returned {@link Streamer} do not affect <em>streamable</em>.
     *
     * @param <E> The type of the contained elements.
     * @throws NullPointerException if the specified <em>streamable</em> is {@code null}.
     */
    public static <E> Streamer<E> by(final Streamable<E> streamable) {
        return new Streamer<>(streamable);
    }

    private Streamer<E> setup(final Consumer<List<E>> consumer) {
        consumer.accept(backing);
        return this;
    }

    /**
     * Returns a sequential {@code Stream} over the elements currently containedin <em>this</em> {@link Streamer}.
     */
    @Override
    public final Stream<E> stream() {
        return backing.stream();
    }

    /**
     * {@inheritDoc}
     * <p>
     * This implementation returns <em>this</em>.
     */
    @Override
    public final Streamer<E> add(final E element) {
        return setup(list -> list.add(element));
    }

    /**
     * {@inheritDoc}
     * <p>
     * This implementation returns <em>this</em>.
     *
     * @param <X> The element type of the <em>other</em> {@link Streamable}.
     * @throws NullPointerException if <em>other</em> is {@code null}.
     */
    @Override
    public final <X extends E> Streamer<E> addAll(final Streamable<X> other) {
        return setup(list -> list.addAll(other.toList()));
    }

    /**
     * {@inheritDoc}
     * <p>
     * This implementation returns <em>this</em>.
     */
    @Override
    public final Streamer<E> remove(final Object candidate) {
        return removeIf(element -> Objects.equals(element, candidate));
    }

    /**
     * {@inheritDoc}
     * <p>
     * This implementation returns <em>this</em>.
     *
     * @throws NullPointerException if the specified <em>other</em> {@link Streamable} is {@code null}.
     */
    @Override
    public final <X> Streamer<E> removeAll(final Streamable<X> other) {
        return removeIf(other::contains);
    }

    /**
     * {@inheritDoc}
     * <p>
     * This implementation returns <em>this</em>.
     *
     * @throws NullPointerException if the specified <em>condition</em> is {@code null}.
     */
    @Override
    public final Streamer<E> removeIf(final Predicate<? super E> condition) {
        return setup(list -> list.removeIf(condition));
    }

    /**
     * {@inheritDoc}
     * <p>
     * This implementation returns <em>this</em>.
     *
     * @throws NullPointerException if the specified <em>other</em> {@link Streamer} is {@code null}.
     */
    @Override
    public final <X> Streamer<E> retainAll(final Streamable<X> other) {
        return retainIf(other::contains);
    }

    /**
     * {@inheritDoc}
     * <p>
     * This implementation returns <em>this</em>.
     *
     * @throws NullPointerException if the specified <em>condition</em> is {@code null}.
     */
    @Override
    public final Streamer<E> retainIf(final Predicate<? super E> condition) {
        return setup(list -> list.removeIf(not(condition)));
    }
}
