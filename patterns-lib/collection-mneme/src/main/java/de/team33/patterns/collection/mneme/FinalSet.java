package de.team33.patterns.collection.mneme;

import de.team33.patterns.streamable.galatea.Buffer;
import de.team33.patterns.streamable.galatea.Streamable;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Stream;

/**
 * An immutable {@link Set} implementation
 * that preserves the encounter order of its source and may contain {@code null} elements.
 * <p>
 * To build an instance you may use a {@link Stream} and {@link #collector()}, example:
 * <pre>{@code
 * final FinalSet<String> set = Stream.of("zero", "one", "two")
 *                                    .collect(FinalSet.collector());
 * }</pre>
 *
 * @param <E> the type of elements in that set.
 * @see #empty()
 * @see #of(Object)
 * @see #of(Object, Object, Object[])
 * @see #of(Collection)
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
     *
     * @param <E> the formal type of elements in the resulting set.
     */
    @SuppressWarnings("unchecked")
    public static <E> FinalSet<E> empty() {
        // Already is immutable ...
        // noinspection AssignmentOrReturnOfFieldWithMutableType
        return EMPTY;
    }

    /**
     * Returns a {@link FinalSet} that contains a single given <em>element</em>.
     *
     * @param <E> the type of elements in the resulting set.
     */
    public static <E> FinalSet<E> of(final E element) {
        return new FinalSet<>(Streamable.of(element));
    }

    /**
     * Returns a {@link FinalSet} that contains two or more given <em>elements</em>.
     *
     * @param <E> the type of elements in the resulting set.
     */
    @SafeVarargs
    public static <E> FinalSet<E> of(final E first, final E next, final E... more) {
        return new FinalSet<>(Streamable.of(first, next, more));
    }

    /**
     * Returns a {@link FinalSet} created from the given <em>source</em>.
     *
     * @param <E> the type of elements in the resulting set.
     */
    public static <E> FinalSet<E> of(final E[] source) {
        return new FinalSet<>(Streamable.of(source));
    }

    /**
     * Returns a {@link FinalSet} created from the given <em>source</em>.
     *
     * @param <E> the type of elements in the resulting set.
     */
    public static <E> FinalSet<E> of(final Collection<? extends E> source) {
        return new FinalSet<>(Streamable.cast(source::stream));
    }

    /**
     * Returns a {@link FinalSet} created from the given <em>source</em>.
     *
     * @param <E> the type of elements in the resulting set.
     */
    public static <E> FinalSet<E> of(final Streamable<? extends E> source) {
        return new FinalSet<>(Streamable.cast(source));
    }

    /**
     * Returns a {@link Collector} to {@linkplain Stream#collect(Collector) collect} elements of type {@code <E>}
     * into a new {@link FinalSet}.
     *
     * @param <E> the type of elements in the resulting set.
     */
    public static <E> Collector<E, ?, FinalSet<E>> collector() {
        return Buffer.collector(FinalSet::new);
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
