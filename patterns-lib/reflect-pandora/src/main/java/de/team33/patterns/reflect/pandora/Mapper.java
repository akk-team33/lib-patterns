package de.team33.patterns.reflect.pandora;

import java.util.Map;

public class Mapper<S, T> {

    private final Getters<S> getters;
    private final Setters<T> setters;

    private Mapper(final Getters<S> getters, final Setters<T> setters) {
        this.getters = getters;
        this.setters = setters;
    }

    public static <S, T> Mapper<S, T> mapping(final Class<S> sourceClass, final Class<T> targetClass) {
        return new Mapper<>(Getters.of(sourceClass), Setters.of(targetClass));
    }

    public final T map(final S source, final T target) {
        getters.names()
               .forEach(name -> setters.setter(name, getters.type(name))
                                       .accept(target, getters.getter(name)
                                                              .apply(source)));
        return target;
    }

    public final Map<String, Object> toMap(final S source) {
        return getters.toMap(source);
    }
}
