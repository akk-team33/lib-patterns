package de.team33.patterns.collection.mneme;

import java.util.AbstractList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

/**
 * An immutable, abstract {@link List} implementation that definitely does not support any optional list method.
 */
public abstract class ImmutableList<E> extends AbstractList<E> {

    /**
     * Not supported.
     *
     * @throws UnsupportedOperationException in any case
     */
    @Override
    public final boolean add(final E e) {
        throw new UnsupportedOperationException("FinalList does not support add()");
    }

    /**
     * Not supported.
     *
     * @throws UnsupportedOperationException in any case
     */
    @Override
    public final E set(final int index, final E element) {
        throw new UnsupportedOperationException("FinalList does not support set()");
    }

    /**
     * Not supported.
     *
     * @throws UnsupportedOperationException in any case
     */
    @Override
    public final void add(final int index, final E element) {
        throw new UnsupportedOperationException("FinalList does not support add()");
    }

    /**
     * Not supported.
     *
     * @throws UnsupportedOperationException in any case
     */
    @Override
    public final E remove(final int index) {
        throw new UnsupportedOperationException("FinalList does not support remove()");
    }

    /**
     * Not supported.
     *
     * @throws UnsupportedOperationException in any case
     */
    @Override
    public final void clear() {
        throw new UnsupportedOperationException("FinalList does not support clear()");
    }

    /**
     * Not supported.
     *
     * @throws UnsupportedOperationException in any case
     */
    @Override
    public final boolean addAll(final int index, final Collection<? extends E> c) {
        throw new UnsupportedOperationException("FinalList does not support addAll()");
    }

    /**
     * Not supported.
     *
     * @throws UnsupportedOperationException in any case
     */
    @Override
    public final void removeRange(final int fromIndex, final int toIndex) {
        throw new UnsupportedOperationException("FinalList does not support removeRange()");
    }

    /**
     * Not supported.
     *
     * @throws UnsupportedOperationException in any case
     */
    @Override
    public final boolean remove(final Object o) {
        throw new UnsupportedOperationException("FinalList does not support remove()");
    }

    /**
     * Not supported.
     *
     * @throws UnsupportedOperationException in any case
     */
    @Override
    public final boolean addAll(final Collection<? extends E> c) {
        throw new UnsupportedOperationException("FinalList does not support addAll()");
    }

    /**
     * Not supported.
     *
     * @throws UnsupportedOperationException in any case
     */
    @Override
    public final boolean removeAll(final Collection<?> c) {
        throw new UnsupportedOperationException("FinalList does not support removeAll()");
    }

    /**
     * Not supported.
     *
     * @throws UnsupportedOperationException in any case
     */
    @Override
    public final boolean retainAll(final Collection<?> c) {
        throw new UnsupportedOperationException("FinalList does not support retainAll()");
    }

    /**
     * Not supported.
     *
     * @throws UnsupportedOperationException in any case
     */
    @Override
    public final void replaceAll(final UnaryOperator<E> operator) {
        throw new UnsupportedOperationException("FinalList does not support replaceAll()");
    }

    /**
     * Not supported.
     *
     * @throws UnsupportedOperationException in any case
     */
    @Override
    public final boolean removeIf(final Predicate<? super E> filter) {
        throw new UnsupportedOperationException("FinalList does not support removeIf()");
    }
}
