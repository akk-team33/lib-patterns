package de.team33.patterns.serial.charon;

import java.util.*;
import java.util.function.BiFunction;

/**
 * Abstracts a series of elements of a certain type and as such represents an alternative view of a {@link Collection}.
 * <p>
 * In this view, a series essentially consists of two components:
 * <ul>
 *     <li>The first element ("head", an element that may be missing)</li>
 *     <li>The rest of the sequence ("tail", again a sequence)</li>
 * </ul>
 * <p>
 * If the first component ("head") is missing, the sequence is empty.
 * Then the rest of the sequence ("tail") is also empty.
 * <p>
 * A sequence according to this definition is immutable and cannot contain null elements.
 * In addition to the components mentioned above, it has a certain size (number of items contained),
 * can be streamed and presented as a "normal" {@link List}.
 *
 * @param <E> The element type.
 */
public abstract class Series<E> {

    /**
     * Returns an empty {@link Series}.
     */
    @SuppressWarnings("unchecked")
    public static <E> Series<E> empty() {
        return Empty.INSTANCE;
    }

    public static <E> Series<E> of(final Collection<? extends E> origin) {
        return Charged.seriesOf(new ArrayList<>(origin), 0);
    }

    /**
     * Returns {@code true} if <em>this</em> does not contain any element, {@code false} otherwise.
     *
     * @see #isCharged()
     */
    public final boolean isEmpty() {
        return 0 == size();
    }

    /**
     * Returns {@code true} if <em>this</em> contains at least one element, {@code false} otherwise.
     *
     * @see #isEmpty()
     */
    public final boolean isCharged() {
        return 0 < size();
    }

    /**
     * Executes a given {@linkplain BiFunction function} with <em>this</em> series' {@linkplain #head() head} and
     * {@linkplain #tail() tail} as arguments if <em>this</em> {@linkplain #isCharged() is charged}.
     *
     * @param <R> The result type.
     * @return <ul>
     * <li>An {@link Optional} describing the result of the {@linkplain BiFunction#apply(Object, Object) function call}
     * if <em>this</em> {@linkplain #isCharged() is charged}.</li>
     * <li>{@link Optional#empty()} if the result of the {@linkplain BiFunction#apply(Object, Object) function call}
     * is {@code null}.</li>
     * <li>{@link Optional#empty()} if <em>this</em> {@linkplain #isEmpty() is empty}.</li>
     * </ul>
     *
     * @see #isCharged()
     */
    public final <R> Optional<R> ifCharged(final BiFunction<E, Series<E>, R> function) {
        return Optional.of(this)
                       .filter(Series::isCharged)
                       .map(series -> function.apply(series.head(), series.tail()));
    }

    /**
     * Returns <em>this</em> series' head element if <em>this</em> {@linkplain #isCharged() is charged}.
     *
     * @throws NoSuchElementException if <em>this</em> {@linkplain #isEmpty() is empty}.
     */
    public abstract E head();

    /**
     * Returns the remaining subseries that follows <em>this</em> series' {@linkplain #head() head element}.
     */
    public abstract Series<E> tail();

    /**
     * Returns the number of elements within <em>this</em> series.
     */
    public abstract int size();

    /**
     * Returns an immutable {@link List} representation of <em>this</em> series.
     */
    public abstract List<E> asList();

    @Override
    public final boolean equals(final Object obj) {
        return (this == obj) || ((obj instanceof Series) && asList().equals(((Series<?>) obj).asList()));
    }

    @Override
    public final int hashCode() {
        return asList().hashCode();
    }

    @Override
    public final String toString() {
        return asList().toString();
    }
}
