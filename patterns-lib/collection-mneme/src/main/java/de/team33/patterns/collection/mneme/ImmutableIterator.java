package de.team33.patterns.collection.mneme;

import java.util.Iterator;

/**
 * An immutable, abstract {@link Iterator} implementation that definitely does not support {@link Iterator#remove()}.
 *
 * @param <E> the type of elements to be handled.
 */
public abstract class ImmutableIterator<E> implements Iterator<E> {

    /**
     * Returns an {@link ImmutableIterator} backed by the given <em>iterator</em>.
     *
     * @param <E> the type of elements to be handled.
     */
    public static <E> ImmutableIterator<E> proxy(final Iterator<? extends E> iterator) {
        return new Proxy<>(iterator);
    }

    /**
     * Not supported.
     *
     * @throws UnsupportedOperationException in any case
     */
    @Override
    public final void remove() {
        throw new UnsupportedOperationException("ImmutableIterator does not support remove()");
    }

    private static final class Proxy<E> extends ImmutableIterator<E> {

        private final Iterator<? extends E> core;

        private Proxy(final Iterator<? extends E> core) {
            this.core = core;
        }

        @Override
        public boolean hasNext() {
            return core.hasNext();
        }

        @Override
        public E next() {
            return core.next();
        }
    }
}
