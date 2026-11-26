package de.team33.patterns.enums.pan;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * A Tool to handle enum values.
 *
 * @param <E> The enum type whose values should be handled.
 */
@SuppressWarnings("WeakerAccess")
public final class Values<E extends Enum<E>> {

    private final Class<E> enumClass;

    @SuppressWarnings("BoundedWildcard")
    private Values(final Class<E> enumClass) {
        this.enumClass = enumClass;
    }

    /**
     * Returns a tool instance to handle the values of the given <em>enum class</em>.
     */
    public static <E extends Enum<E>> Values<E> of(final Class<E> enumClass) {
        return new Values<>(enumClass);
    }

    /**
     * Returns a {@link Stream} over all values of the underlying enum type.
     */
    public final Stream<E> stream() {
        return Stream.of(enumClass.getEnumConstants());
    }

    /**
     * Returns a {@link Stream} of those values of the underlying enum type that match the given <em>filter</em>.
     */
    public final Stream<E> findAll(final Predicate<? super E> filter) {
        return stream().filter(filter);
    }

    /**
     * Returns an {@linkplain Optional optional} value of the associated enum type that matches the given
     * <em>filter</em> or, if no such value exists, {@link Optional#empty()}.
     */
    public final Optional<E> findAny(final Predicate<? super E> filter) {
        return findAll(filter).findAny();
    }

    /**
     * Returns a value of the associated enum type that matches the given <em>filter</em> or,
     * if no such value exists, the given <em>fallback</em>.
     */
    public final E findAny(final Predicate<? super E> filter, final E fallback) {
        return findAny(filter).orElse(fallback);
    }

    /**
     * Returns the {@linkplain Optional optional} first value of the associated enum type that matches the given
     * <em>filter</em> or, if no such value exists, {@link Optional#empty()}.
     */
    public final Optional<E> findFirst(final Predicate<? super E> filter) {
        return findAll(filter).findFirst();
    }

    /**
     * Returns the first value of the associated enum type that matches the given <em>filter</em> or,
     * if no such value exists, the given <em>fallback</em>.
     */
    public final E findFirst(final Predicate<? super E> filter, final E fallback) {
        return findFirst(filter).orElse(fallback);
    }

    /**
     * Just like {@link #stream()}.{@link Stream#map(Function) map(mapping)}.
     */
    public final <T> Stream<T> mapAll(final Function<? super E, ? extends T> mapping) {
        return stream().map(mapping);
    }

    /**
     * Just like {@link #findAll(Predicate) findAll(filter)}.{@link Stream#map(Function) map(mapping)}.
     */
    public final <T> Stream<T> mapAll(final Predicate<? super E> filter,
                                      final Function<? super E, ? extends T> mapping) {
        return findAll(filter).map(mapping);
    }

    /**
     * Just like {@link #findAny(Predicate) findAny(filter)}.{@link Stream#map(Function) map(mapping)}.
     */
    public final <R> Optional<R> mapAny(final Predicate<? super E> filter,
                                        final Function<? super E, ? extends R> mapping) {
        return findAny(filter).map(mapping);
    }

    /**
     * Just like {@link #findFirst(Predicate) findFirst(filter)}.{@link Stream#map(Function) map(mapping)}.
     */
    public final <R> Optional<R> mapFirst(final Predicate<? super E> filter,
                                          final Function<? super E, ? extends R> mapping) {
        return findFirst(filter).map(mapping);
    }
}
