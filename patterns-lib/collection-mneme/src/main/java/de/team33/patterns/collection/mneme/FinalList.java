package de.team33.patterns.collection.mneme;

import de.team33.patterns.streamable.naiad.Streamable;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

/**
 * An immutable {@link List} implementation that may contain {@code null} elements.
 */
@SuppressWarnings("MethodOverridesStaticMethodOfSuperclass")
public final class FinalList<E> extends ImmutableList<E> {

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static final FinalList EMPTY = new FinalList(Stream.empty());

    private final List<E> core;

    private FinalList(final Stream<E> source) {
        this.core = source.toList();
    }

    /**
     * Returns a {@link FinalList} collected from the given <em>source</em>.
     * <p>
     * The <em>source</em> is subsequently terminated.
     *
     * @throws NullPointerException  if <em>source</em> is {@code null}
     * @throws IllegalStateException if <em>source</em> is already terminated
     */
    public static <E> FinalList<E> collect(final Stream<? extends E> source) {
        return new FinalList<>(source.map(e -> (E) e));
    }

    /**
     * Returns an empty {@link FinalList}.
     */
    public static <E> FinalList<E> empty() {
        // Already is immutable ...
        // noinspection AssignmentOrReturnOfFieldWithMutableType,unchecked
        return EMPTY;
    }

    /**
     * Returns a {@link FinalList} that contains a single given <em>element</em>.
     */
    public static <E> FinalList<E> of(final E element) {
        return new FinalList<>(Stream.of(element));
    }

    /**
     * Returns a {@link FinalList} that contains two or more given <em>elements</em>.
     */
    @SafeVarargs
    public static <E> FinalList<E> of(final E first, final E next, final E... more) {
        return new FinalList<>(Stream.concat(Stream.of(first, next), Stream.of(more)));
    }

    /**
     * Returns a {@link FinalList} created from the given <em>source</em>.
     *
     * @throws NullPointerException if <em>source</em> is {@code null}
     */
    public static <E> FinalList<E> of(final E[] source) {
        return new FinalList<>(Stream.of(source));
    }

    /**
     * Returns a {@link FinalList} created from the given <em>source</em>.
     */
    public static <E> FinalList<E> of(final Collection<? extends E> source) {
        return collect(source.stream());
    }

    /**
     * @deprecated use {@link #collect(Stream)} instead.
     */
    @Deprecated(forRemoval = true)
    public static <E> FinalList<E> of(final Streamable<? extends E> source) {
        return collect(source.stream());
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
