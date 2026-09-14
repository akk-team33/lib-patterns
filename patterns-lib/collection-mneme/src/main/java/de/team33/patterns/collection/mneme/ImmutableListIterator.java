package de.team33.patterns.collection.mneme;

import java.util.ListIterator;

/**
 * An immutable, abstract {@link ListIterator} implementation that definitely does not support
 * {@link ListIterator#remove()}, {@link ListIterator#set(Object)} and {@link ListIterator#add(Object)}.
 *
 * @param <E> the type of elements to be handled.
 */
public abstract class ImmutableListIterator<E> extends ImmutableIterator<E> implements ListIterator<E> {

    /**
     * Returns an {@link ImmutableListIterator} backed by the given <em>iterator</em>.
     *
     * @param <E> the type of elements to be handled.
     */
    public static <E> ImmutableListIterator<E> proxy(final ListIterator<? extends E> iterator) {
        return new Proxy<>(iterator);
    }

    /**
     * Not supported.
     *
     * @throws UnsupportedOperationException in any case
     */
    @Override
    public final void set(final E e) {
        throw new UnsupportedOperationException("ImmutableListIterator does not support set()");
    }

    /**
     * Not supported.
     *
     * @throws UnsupportedOperationException in any case
     */
    @Override
    public final void add(final E e) {
        throw new UnsupportedOperationException("ImmutableListIterator does not support add()");
    }

    private static final class Proxy<E> extends ImmutableListIterator<E> {

        private final ListIterator<? extends E> core;

        private Proxy(final ListIterator<? extends E> core) {
            this.core = core;
        }

        @Override
        public boolean hasPrevious() {
            return core.hasPrevious();
        }

        @Override
        public E previous() {
            return core.previous();
        }

        @Override
        public int previousIndex() {
            return core.previousIndex();
        }

        @Override
        public boolean hasNext() {
            return core.hasNext();
        }

        @Override
        public E next() {
            return core.next();
        }

        @Override
        public int nextIndex() {
            return core.nextIndex();
        }
    }
}
