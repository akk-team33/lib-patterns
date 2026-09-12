package de.team33.patterns.streamable.galatea;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A mutable {@link Streamable} implementation that provides a builder pattern.
 *
 * @param <E> The type of contained elements.
 */
public final class Buffer<E> implements Streamable<E> {

    private final List<E> core;

    private Buffer(final Streamable<E> origin) {
        this.core = origin.stream().collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Returns a {@link Buffer} that {@linkplain #isEmpty() is empty}.
     *
     * @param <E> The type of virtually contained elements.
     */
    public static <E> Buffer<E> empty() {
        return by(Streamable.empty());
    }

    /**
     * Returns a {@link Buffer} that contains a single given <em>element</em>.
     *
     * @param <E> The type of the contained element.
     */
    public static <E> Buffer<E> of(final E element) {
        return by(Streamable.of(element));
    }

    /**
     * Returns a {@link Buffer} that contains two or more given <em>elements</em>.
     *
     * @param <E> The type of the contained elements.
     */
    @SafeVarargs
    public static <E> Buffer<E> of(final E element0, final E element1, final E... more) {
        return by(Streamable.of(element0, element1, more));
    }

    /**
     * Returns a {@link Buffer} backed by an array of <em>elements</em>.
     *
     * @param <E> The type of the contained elements.
     */
    public static <E> Buffer<E> of(final E[] elements) {
        return by(Streamable.of(elements));
    }

    /**
     * Returns a {@link Buffer} backed by a given {@link Iterable}.
     *
     * @param <E> The type of the contained elements.
     */
    public static <E> Buffer<E> of(final Iterable<E> iterable) {
        return by(Streamable.of(iterable));
    }

    /**
     * Returns a {@link Buffer} backed by a given {@link Streamable}.
     *
     * @param <E> The type of the contained elements.
     */
    public static <E> Buffer<E> by(final Streamable<E> streamable) {
        return new Buffer<>(streamable);
    }

    private Buffer<E> setup(final Consumer<? super List<E>> consumer) {
        consumer.accept(core);
        return this;
    }

    /**
     * Returns a sequential {@code Stream} over the elements currently contained in <em>this</em> {@link Buffer}.
     */
    @Override
    public final Stream<E> stream() {
        return core.stream();
    }

    /**
     * Appends a single <em>element</em> to <em>this</em> buffer.
     *
     * @return <em>this</em>
     */
    public final Buffer<E> add(final E element) {
        return setup(list -> list.add(element));
    }

    /**
     * Appends all given <em>elements</em> to <em>this</em> buffer.
     *
     * @param <X> The element type of the <em>elements</em>.
     * @return <em>this</em>
     * @throws NullPointerException if <em>elements</em> is {@code null}.
     */
    public final <X extends E> Buffer<E> addAll(final Streamable<X> elements) {
        return setup(list -> list.addAll(elements.toList()));
    }

    /**
     * Removes each element from <em>this</em> buffer that equals the given <em>candidate</em>.
     *
     * @return <em>this</em>
     */
    public final Buffer<E> remove(final Object candidate) {
        return removeAll(Streamable.of(candidate));
    }

    /**
     * Removes each element from <em>this</em> buffer that equals one of the given <em>candidates</em>.
     *
     * @return <em>this</em>
     * @throws NullPointerException if <em>candidates</em> is {@code null}.
     */
    public final <X> Buffer<E> removeAll(final Streamable<X> candidates) {
        //noinspection SuspiciousMethodCalls
        return setup(list -> list.removeAll(candidates.toList()));
    }

    /**
     * Removes each element from <em>this</em> buffer that meets the given <em>condition</em>.
     *
     * @return <em>this</em>
     * @throws NullPointerException if the <em>condition</em> is {@code null}.
     */
    public final Buffer<E> removeIf(final Predicate<? super E> condition) {
        return setup(list -> list.removeIf(condition));
    }

    /**
     * Removes each element from <em>this</em> buffer that equals none of the given <em>candidates</em>.
     *
     * @return <em>this</em>
     * @throws NullPointerException if <em>candidates</em> is {@code null}.
     */
    public final <X> Buffer<E> retainAll(final Streamable<X> candidates) {
        //noinspection SuspiciousMethodCalls
        return setup(list -> list.retainAll(candidates.toList()));
    }

    /**
     * Removes each element from <em>this</em> buffer that does not meet the given <em>condition</em>.
     *
     * @return <em>this</em>
     * @throws NullPointerException if the <em>condition</em> is {@code null}.
     */
    public final Buffer<E> retainIf(final Predicate<? super E> condition) {
        return removeIf(condition.negate());
    }
}
