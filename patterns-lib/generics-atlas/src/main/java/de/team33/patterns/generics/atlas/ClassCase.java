package de.team33.patterns.generics.atlas;

import java.util.function.Function;

enum ClassCase {

    CLASS(ClassSetup::new),
    ARRAY(PlainArraySetup::new);

    private final Function<Class<?>, Setup> mapping;

    ClassCase(final Function<Class<?>, Setup> mapping) {
        this.mapping = mapping;
    }

    static Setup toStage(final Class<?> underlyingClass) {
        return (underlyingClass.isArray() ? ARRAY : CLASS).mapping.apply(underlyingClass);
    }
}
