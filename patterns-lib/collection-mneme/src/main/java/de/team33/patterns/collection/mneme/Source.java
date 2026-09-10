package de.team33.patterns.collection.mneme;

import java.util.Map;
import java.util.stream.Stream;

@FunctionalInterface
interface Source<E> {

    static <E> Source<E> empty() {
        return Stream::empty;
    }

    static <E> Source<E> of(final E element) {
        return () -> Stream.of(element);
    }

    @SafeVarargs
    static <E> Source<E> of(final E element0, final E element1, final E... more) {
        return () -> Stream.concat(Stream.of(element0, element1), Stream.of(more));
    }

    static <E> Source<E> of(final E[] elements) {
        return () -> Stream.of(elements);
    }

    static <K, V> Source<Map.Entry<K, V>> of(final Map<K, V> map) {
        return () -> map.entrySet().stream();
    }

    @SuppressWarnings("unchecked")
    static <E, X extends E> Source<E> cast(final Source<X> source) {
        return (Source<E>) source;
    }

    Stream<E> stream();
}
