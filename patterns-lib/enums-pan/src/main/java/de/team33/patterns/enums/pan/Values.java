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
public class Values<E extends Enum<E>> {

    private final Class<E> enumClass;

    private Values(final Class<E> enumClass) {
        this.enumClass = enumClass;
    }

    /**
     * Returns a tool to handle the values of the given <em>enum class</em>.
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

    public final E findAny(final Predicate<? super E> filter, final E fallback) {
        return findAny(filter).orElse(fallback);
    }

    public final Optional<E> findAny(final Predicate<? super E> filter) {
        return findAll(filter).findAny();
    }

    public final E findFirst(final Predicate<? super E> filter, final E fallback) {
        return findFirst(filter).orElse(fallback);
    }

    public final Optional<E> findFirst(final Predicate<? super E> filter) {
        return findAll(filter).findFirst();
    }

    public final <T> Stream<T> mapAll(final Function<E, T> mapping) {
        return stream().map(mapping);
    }

    public final <R> Optional<R> mapAny(final Predicate<? super E> filter,
                                        final Function<? super E, ? extends R> mapping) {
        return findAny(filter).map(mapping);
    }

    public final <R> Optional<R> mapFirst(final Predicate<? super E> filter,
                                          final Function<? super E, ? extends R> mapping) {
        return findFirst(filter).map(mapping);
    }
}
