package de.team33.patterns.collection.mneme;

import java.util.AbstractSet;
import java.util.Set;

/**
 * An immutable, abstract {@link Set} implementation that definitely does not support any optional set method.
 *
 * @param <E> the type of elements in that set.
 */
public abstract class ImmutableSet<E> extends ImmutableCollection<E> implements Set<E> {

    @SuppressWarnings({"TypeMayBeWeakened", "MismatchedQueryAndUpdateOfCollection"})
    private final SetSupport support = new SetSupport();

    /**
     * Returns an {@link ImmutableSet} backed by the given <em>set</em>,
     * preserving the encounter order of the <em>set</em>, if any.
     *
     * @param <E> the type of elements in the result.
     */
    public static <E> ImmutableSet<E> proxy(final Set<? extends E> set) {
        return new Proxy<>(set);
    }

    /**
     * Returns {@code true} if <em>obj</em> is a {@link Set} that contains the same elements as <em>this</em> set.
     *
     * @see Set#equals(Object)
     */
    @Override
    public final boolean equals(final Object obj) {
        return (this == obj) || support.equals(obj);
    }

    /**
     * Returns a hash code that is consistent with {@link #equals(Object)}.
     *
     * @see Set#hashCode()
     */
    @Override
    public final int hashCode() {
        return support.hashCode();
    }

    private static final class Proxy<E> extends ImmutableSet<E> {

        private final Set<? extends E> core;

        private Proxy(final Set<? extends E> core) {
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

    private final class SetSupport extends AbstractSet<E> {

        @Override
        public ImmutableIterator<E> iterator() {
            return ImmutableSet.this.iterator();
        }

        @Override
        public int size() {
            return ImmutableSet.this.size();
        }
    }
}
