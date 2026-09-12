package de.team33.patterns.streamable.galatea.sample;

import de.team33.patterns.streamable.galatea.Streamable;

import java.util.Collection;
import java.util.Iterator;
import java.util.function.Consumer;
import java.util.stream.Stream;

@SuppressWarnings({"unused", "InterfaceNeverImplemented"})
@FunctionalInterface
public interface StreamableCollectionProxy<E> extends Streamable<E>, Collection<E> {

    Collection<E> backing();

    @Override
    default Stream<E> stream() {
        return backing().stream();
    }

    @Override
    default int size() {
        return backing().size();
    }

    @Override
    default boolean isEmpty() {
        return backing().isEmpty();
    }

    @Override
    default boolean contains(final Object candidate) {
        return backing().contains(candidate);
    }

    @Override
    default Iterator<E> iterator() {
        return backing().iterator();
    }

    @Override
    default Object[] toArray() {
        return backing().toArray();
    }

    @Override
    default <T> T[] toArray(final T[] a) {
        return backing().toArray(a);
    }

    @Override
    default boolean add(final E e) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    default boolean remove(final Object o) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    default boolean containsAll(final Collection<?> other) {
        return backing().containsAll(other);
    }

    @Override
    default boolean addAll(final Collection<? extends E> other) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    default boolean removeAll(final Collection<?> other) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    default boolean retainAll(final Collection<?> other) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    default void clear() {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    default void forEach(final Consumer<? super E> action) {
        backing().forEach(action);
    }
}
