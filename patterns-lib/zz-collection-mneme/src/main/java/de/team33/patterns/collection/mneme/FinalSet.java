package de.team33.patterns.collection.mneme;

import de.team33.patterns.streamable.naiad.Streamable;

import java.util.*;

/**
 * An immutable {@link Set} implementation
 * that preserves the encounter order of its source and may contain {@code null} elements.
 *
 * @param <E> the type of elements in this set.
 */
@SuppressWarnings("MethodOverridesStaticMethodOfSuperclass")
public final class FinalSet<E> extends AbstractSet<E> {

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static final FinalSet EMPTY = new FinalSet(Streamable.empty());

    private final List<E> core;

    private FinalSet(final Streamable<E> source) {
        this.core = source.stream().distinct().toList();
    }

    /**
     * Returns an empty {@link FinalSet}.
     */
    @SuppressWarnings("unchecked")
    public static <E> FinalSet<E> empty() {
        // Already is immutable ...
        // noinspection AssignmentOrReturnOfFieldWithMutableType
        return EMPTY;
    }

    /**
     * Returns a {@link FinalSet} that contains a single given <em>element</em>.
     */
    public static <E> FinalSet<E> of(final E element) {
        return new FinalSet<>(Streamable.of(element));
    }

    /**
     * Returns a {@link FinalSet} that contains two or more given <em>elements</em>.
     */
    @SafeVarargs
    public static <E> FinalSet<E> of(final E first, final E next, final E... more) {
        return new FinalSet<>(Streamable.of(first, next, more));
    }

    /**
     * Returns a {@link FinalSet} created from the given <em>source</em>.
     */
    public static <E> FinalSet<E> of(final E[] source) {
        return new FinalSet<>(Streamable.of(source));
    }

    /**
     * Returns a {@link FinalSet} created from the given <em>source</em>.
     */
    public static <E> FinalSet<E> of(final Collection<? extends E> source) {
        return new FinalSet<>(Streamable.cast(source::stream));
    }

    /**
     * Returns a {@link FinalSet} created from the given <em>source</em>.
     */
    public static <E> FinalSet<E> of(final Streamable<? extends E> source) {
        return new FinalSet<>(Streamable.cast(source));
    }

    @Override
    public final Iterator<E> iterator() {
        return core.iterator();
    }

    @Override
    public final int size() {
        return core.size();
    }
}
