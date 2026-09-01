package de.team33.patterns.collection.mneme;

import de.team33.patterns.streamable.naiad.Streamable;

import java.util.AbstractList;
import java.util.Collection;
import java.util.List;

/**
 * An immutable {@link List} implementation that may contain {@code null} elements.
 *
 * @param <E> the type of elements in this list.
 */
@SuppressWarnings("MethodOverridesStaticMethodOfSuperclass")
public final class FinalList<E> extends AbstractList<E> {

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static final FinalList EMPTY = new FinalList(Streamable.empty());

    private final List<E> core;

    private FinalList(final Streamable<E> source) {
        this.core = source.stream().toList();
    }

    /**
     * Returns an empty {@link FinalList}.
     */
    @SuppressWarnings({"unchecked", "AssignmentOrReturnOfFieldWithMutableType"})
    public static <E> FinalList<E> empty() {
        return EMPTY;
    }

    /**
     * Returns a {@link FinalList} that contains a single given <em>element</em>.
     */
    public static <E> FinalList<E> of(final E element) {
        return new FinalList<>(Streamable.of(element));
    }

    /**
     * Returns a {@link FinalList} that contains two or more given <em>elements</em>.
     */
    @SafeVarargs
    public static <E> FinalList<E> of(final E first, final E next, final E... more) {
        return new FinalList<>(Streamable.of(first, next, more));
    }

    /**
     * Returns a {@link FinalList} created from the given <em>source</em>.
     */
    public static <E> FinalList<E> of(final E[] source) {
        return new FinalList<>(Streamable.of(source));
    }

    /**
     * Returns a {@link FinalList} created from the given <em>source</em>.
     */
    public static <E> FinalList<E> of(final Collection<? extends E> source) {
        return new FinalList<>(Streamable.cast(source::stream));
    }

    /**
     * Returns a {@link FinalList} created from the given <em>source</em>.
     */
    public static <E> FinalList<E> of(final Streamable<? extends E> source) {
        return new FinalList<>(Streamable.cast(source));
    }

    @Override
    public final E get(final int index) {
        return core.get(index);
    }

    @Override
    public final int size() {
        return core.size();
    }
}
