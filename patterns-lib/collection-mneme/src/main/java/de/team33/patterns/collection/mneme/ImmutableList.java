package de.team33.patterns.collection.mneme;

import java.util.AbstractList;
import java.util.Collection;
import java.util.List;
import java.util.function.UnaryOperator;

/**
 * An immutable, abstract {@link List} implementation that definitely does not support any optional list method.
 *
 * @param <E> the type of elements in that list.
 */
public abstract class ImmutableList<E> extends ImmutableCollection<E> implements List<E> {

    @SuppressWarnings("TypeMayBeWeakened")
    private final ListSupport support = new ListSupport();

    /**
     * Returns an {@link ImmutableList} backed by the given <em>list</em>.
     * <p>
     * Returns <em>list</em> if it is already an {@link ImmutableList}.
     *
     * @param <E> the type of elements in the result.
     */
    public static <E> ImmutableList<E> proxy(final List<? extends E> list) {
        //noinspection rawtypes
        if (list instanceof final ImmutableList immutable) {
            //noinspection unchecked
            return immutable;
        } else {
            return new Proxy<>(list);
        }
    }

    /**
     * Returns the index of the first occurrence of the specified <em>element</em> in <em>this</em> list,
     * or -1 if <em>this</em> list does not contain the <em>element</em>.
     */
    @Override
    public final int indexOf(final Object element) {
        return support.indexOf(element);
    }

    /**
     * Returns the index of the last occurrence of the specified <em>element</em> in <em>this</em> list,
     * or -1 if <em>this</em> list does not contain the <em>element</em>.
     */
    @Override
    public final int lastIndexOf(final Object element) {
        return support.lastIndexOf(element);
    }

    /**
     * Returns a view of the portion of this list between the specified <em>fromIndex</em>, inclusive,
     * and <em>toIndex</em>, exclusive.
     * <p>
     * If <em>fromIndex</em> and <em>toIndex</em> are equal, {@linkplain List#isEmpty() is empty}.
     * <p>
     * An {@link ImmutableList} returns an {@link ImmutableList}.
     */
    @Override
    public final ImmutableList<E> subList(final int fromIndex, final int toIndex) {
        return proxy(support.subList(fromIndex, toIndex));
    }

    /**
     * Returns an iterator over the elements in <em>this</em> list.
     * <p>
     * An {@link ImmutableList} returns an {@link ImmutableIterator}.
     */
    @Override
    public final ImmutableIterator<E> iterator() {
        return listIterator(0);
    }

    /**
     * Returns a list iterator over the elements in <em>this</em> list.
     * <p>
     * An {@link ImmutableList} returns an {@link ImmutableListIterator}.
     */
    @Override
    public final ImmutableListIterator<E> listIterator() {
        return listIterator(0);
    }

    /**
     * Returns a list iterator over the elements in <em>this</em> list
     * starting at the given <em>index</em> position.
     * <p>
     * An {@link ImmutableList} returns an {@link ImmutableListIterator}.
     */
    @Override
    public final ImmutableListIterator<E> listIterator(final int index) {
        return ImmutableListIterator.proxy(support.listIterator(index));
    }

    /**
     * Not supported.
     *
     * @throws UnsupportedOperationException in any case
     */
    @Override
    public final void add(final int index, final E element) {
        throw new UnsupportedOperationException("ImmutableList does not support add()");
    }

    /**
     * Not supported.
     *
     * @throws UnsupportedOperationException in any case
     */
    @Override
    public final boolean addAll(final int index, final Collection<? extends E> c) {
        throw new UnsupportedOperationException("ImmutableList does not support addAll()");
    }

    /**
     * Not supported.
     *
     * @throws UnsupportedOperationException in any case
     */
    @Override
    public final E set(final int index, final E element) {
        throw new UnsupportedOperationException("ImmutableList does not support set()");
    }

    /**
     * Not supported.
     *
     * @throws UnsupportedOperationException in any case
     */
    @Override
    public final E remove(final int index) {
        throw new UnsupportedOperationException("ImmutableList does not support remove()");
    }

    /**
     * Not supported.
     *
     * @throws UnsupportedOperationException in any case
     */
    @Override
    public final void replaceAll(final UnaryOperator<E> operator) {
        throw new UnsupportedOperationException("ImmutableList does not support replaceAll()");
    }

    private static final class Proxy<E> extends ImmutableList<E> {

        private final List<? extends E> core;

        private Proxy(final List<? extends E> core) {
            this.core = core;
        }

        @Override
        public E get(final int index) {
            return core.get(index);
        }

        @Override
        public int size() {
            return core.size();
        }
    }

    private final class ListSupport extends AbstractList<E> {

        @Override
        public E get(final int index) {
            return ImmutableList.this.get(index);
        }

        @Override
        public int size() {
            return ImmutableList.this.size();
        }
    }
}
