package de.team33.patterns.collection.mneme;

import de.team33.patterns.streamable.galatea.Buffer;
import de.team33.patterns.streamable.galatea.Streamable;

import java.util.Collection;
import java.util.List;
import java.util.RandomAccess;
import java.util.stream.Collector;
import java.util.stream.Stream;

/**
 * A {@link RandomAccess} {@link ImmutableList} implementation that may contain {@code null} elements.
 * <p>
 * To build an instance you may use a {@link Stream} and {@link #collector()}, example:
 * <pre>{@code
 * final FinalList<String> list = Stream.of("zero", "one", "two")
 *                                      .collect(FinalList.collector());
 * }</pre>
 *
 * @param <E> the type of elements in that list.
 * @see #empty()
 * @see #of(Object)
 * @see #of(Object, Object, Object[])
 * @see #of(Collection)
 * @see #of(Streamable)
 */
@SuppressWarnings("MethodOverridesStaticMethodOfSuperclass")
public final class FinalList<E> extends ImmutableList<E> implements RandomAccess {

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static final FinalList EMPTY = new FinalList(Streamable.empty());

    private final List<E> core;

    private FinalList(final Streamable<E> source) {
        this.core = source.stream().toList();
    }

    /**
     * Returns an empty {@link FinalList}.
     *
     * @param <E> the formal type of elements in the resulting list.
     */
    @SuppressWarnings("unchecked")
    public static <E> FinalList<E> empty() {
        // Already is immutable ...
        // noinspection AssignmentOrReturnOfFieldWithMutableType
        return EMPTY;
    }

    /**
     * Returns a {@link FinalList} that contains a single given <em>element</em>.
     *
     * @param <E> the type of elements in the resulting list.
     */
    public static <E> FinalList<E> of(final E element) {
        return new FinalList<>(Streamable.of(element));
    }

    /**
     * Returns a {@link FinalList} that contains two or more given <em>elements</em>.
     *
     * @param <E> the type of elements in the resulting list.
     */
    @SafeVarargs
    public static <E> FinalList<E> of(final E first, final E next, final E... more) {
        return new FinalList<>(Streamable.of(first, next, more));
    }

    /**
     * Returns a {@link FinalList} created from the given <em>source</em>.
     *
     * @param <E> the type of elements in the resulting list.
     */
    public static <E> FinalList<E> of(final E[] source) {
        return new FinalList<>(Streamable.of(source));
    }

    /**
     * Returns a {@link FinalList} created from the given <em>source</em>.
     *
     * @param <E> the type of elements in the resulting list.
     */
    public static <E> FinalList<E> of(final Collection<? extends E> source) {
        return new FinalList<>(Streamable.cast(source::stream));
    }

    /**
     * Returns a {@link FinalList} created from the given <em>source</em>.
     *
     * @param <E> the type of elements in the resulting list.
     */
    public static <E> FinalList<E> of(final Streamable<? extends E> source) {
        return new FinalList<>(Streamable.cast(source));
    }

    /**
     * Returns a {@link Collector} to {@linkplain Stream#collect(Collector) collect} elements of type {@code <E>}
     * into a new {@link FinalList}.
     *
     * @param <E> the type of elements in the resulting list.
     */
    public static <E> Collector<E, ?, FinalList<E>> collector() {
        return Buffer.collector(FinalList::new);
    }

    /**
     * Returns the element at the given <em>index</em> position in <em>this</em> list.
     *
     * @throws IndexOutOfBoundsException if the <em>index</em> is out of [0 ... ({@link #size()} - 1)]
     */
    @Override
    public final E get(final int index) {
        return core.get(index);
    }

    /**
     * Returns the number of elements in <em>this</em> list.
     */
    @Override
    public final int size() {
        return core.size();
    }
}
