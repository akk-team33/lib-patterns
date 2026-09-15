package de.team33.patterns.collection.mneme;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.function.Predicate;

/**
 * An immutable, abstract {@link Collection} implementation
 * that definitely does not support any optional collection method.
 *
 * @param <E> the type of elements in that collection.
 */
public abstract class ImmutableCollection<E> extends AbstractCollection<E> {

    /**
     * Returns an {@link ImmutableCollection} backed by the given <em>collection</em>,
     * preserving the encounter order of the <em>collection</em>, if any.
     * <p>
     * Returns <em>collection</em> if it is already an {@link ImmutableCollection}.
     *
     * @param <E> the type of elements in the result.
     */
    public static <E> ImmutableCollection<E> proxy(final Collection<? extends E> collection) {
        //noinspection rawtypes
        if (collection instanceof final ImmutableCollection immutable) {
            //noinspection unchecked
            return immutable;
        } else {
            return new Proxy<>(collection);
        }
    }

    /**
     * Returns an iterator over the elements in <em>this</em> collection.
     * <p>
     * An {@link ImmutableCollection} returns an {@link ImmutableIterator}.
     */
    @Override
    public abstract ImmutableIterator<E> iterator();

    /**
     * Not supported.
     *
     * @throws UnsupportedOperationException in any case
     */
    @Override
    public final boolean add(final E e) {
        throw new UnsupportedOperationException("ImmutableCollection does not support add()");
    }

    /**
     * Not supported.
     *
     * @throws UnsupportedOperationException in any case
     */
    @Override
    public final boolean addAll(final Collection<? extends E> c) {
        throw new UnsupportedOperationException("ImmutableCollection does not support addAll()");
    }

    /**
     * Not supported.
     *
     * @throws UnsupportedOperationException in any case
     */
    @Override
    public final boolean remove(final Object o) {
        throw new UnsupportedOperationException("ImmutableCollection does not support remove()");
    }

    /**
     * Not supported.
     *
     * @throws UnsupportedOperationException in any case
     */
    @Override
    public final boolean removeAll(final Collection<?> c) {
        throw new UnsupportedOperationException("ImmutableCollection does not support removeAll()");
    }

    /**
     * Not supported.
     *
     * @throws UnsupportedOperationException in any case
     */
    @Override
    public final boolean removeIf(final Predicate<? super E> filter) {
        throw new UnsupportedOperationException("ImmutableCollection does not support removeIf()");
    }

    /**
     * Not supported.
     *
     * @throws UnsupportedOperationException in any case
     */
    @Override
    public final boolean retainAll(final Collection<?> c) {
        throw new UnsupportedOperationException("ImmutableCollection does not support retainAll()");
    }

    /**
     * Not supported.
     *
     * @throws UnsupportedOperationException in any case
     */
    @Override
    public final void clear() {
        throw new UnsupportedOperationException("ImmutableCollection does not support clear()");
    }

    private static final class Proxy<E> extends ImmutableCollection<E> {

        private final Collection<? extends E> core;

        private Proxy(final Collection<? extends E> core) {
            this.core = core;
        }

        @Override
        public ImmutableIterator<E> iterator() {
            return ImmutableIterator.proxy(core.iterator());
        }

        @Override
        public int size() {
            return core.size();
        }
    }
}
