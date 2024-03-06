package de.team33.patterns.typing.atlas;

import java.util.function.Function;

enum ClassCase {

    CLASS(ClassTypedef::new),
    ARRAY(PlainArrayTypedef::new);

    private final Function<Class<?>, Typedef> mapping;

    ClassCase(final Function<Class<?>, Typedef> mapping) {
        this.mapping = mapping;
    }

    static Typedef toTypedef(final Class<?> underlyingClass) {
        return (underlyingClass.isArray() ? ARRAY : CLASS).mapping.apply(underlyingClass);
    }
}