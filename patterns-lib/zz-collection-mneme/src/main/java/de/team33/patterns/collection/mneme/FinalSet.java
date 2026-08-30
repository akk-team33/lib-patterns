package de.team33.patterns.collection.mneme;

import de.team33.patterns.streamable.naiad.Streamable;

import java.util.*;

/**
 * An immutable {@link Set} that preserves the encounter order of its source.
 * <p>
 * Use {@link #of(Streamable)}, {@link #of(Collection)} or {@link #empty()} to create an instance.
 * <p>
 * The iteration order of a {@code FinalSet} is the order in which the distinct elements are encountered in the source.
 *
 * @param <E> the type of elements in this set.
 */
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
    @SuppressWarnings({"unchecked", "AssignmentOrReturnOfFieldWithMutableType"})
    public static <E> FinalSet<E> empty() {
        return EMPTY;
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
