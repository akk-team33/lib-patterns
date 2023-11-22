package de.team33.patterns.reflect.pandora;

/**
 * Represents a tool that can transfer the properties of a source instance to a target instance,
 * where both must be instances of data object types whose properties are represented by getters and setters.
 * The data object types can be different, but must have compatible properties.
 *
 * @param <S> The source type.
 * @param <T> The target type.
 */
public class Mapper<S, T> {

    private final Getters<S> getters;
    private final Setters<T> setters;

    private Mapper(final Getters<S> getters, final Setters<T> setters) {
        this.getters = getters;
        this.setters = setters;
    }

    /**
     * Returns a mapper that can map properties from a source type to a target type.
     *
     * @param <S> The source type.
     * @param <T> The target type.
     */
    public static <S, T> Mapper<S, T> mapping(final Class<S> sourceClass, final Class<T> targetClass) {
        return mapping(Getters.of(sourceClass), Setters.of(targetClass));
    }

    /**
     * Returns a mapper that can map properties from a source type to a target type using given {@link Getters}.
     *
     * @param <S> The source type.
     * @param <T> The target type.
     */
    public static <S, T> Mapper<S, T> mapping(final Getters<S> getters, final Class<T> targetClass) {
        return mapping(getters, Setters.of(targetClass));
    }

    /**
     * Returns a mapper that can map properties from a source type to a target type using given {@link Getters}
     * and {@link Setters}.
     *
     * @param <S> The source type.
     * @param <T> The target type.
     */
    public static <S, T> Mapper<S, T> mapping(final Getters<S> getters, final Setters<T> setters) {
        return new Mapper<>(getters, setters);
    }

    /**
     * Maps the properties of a source instance to a target instance and returns the target instance.
     */
    public final T map(final S source, final T target) {
        getters.names()
               .forEach(name -> setters.setter(name, getters.type(name))
                                       .accept(target, getters.getter(name)
                                                              .apply(source)));
        return target;
    }
}
