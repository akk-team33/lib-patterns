package de.team33.patterns.typing.atlas;

import java.util.function.Function;

enum ClassCase {

    CLASS(ClassType::new),
    ARRAY(PlainArrayType::new);

    private final Function<Class<?>, Type> mapping;

    ClassCase(final Function<Class<?>, Type> mapping) {
        this.mapping = mapping;
    }

    static Type toTypedef(final Class<?> underlyingClass) {
        return (underlyingClass.isArray() ? ARRAY : CLASS).mapping.apply(underlyingClass);
    }
}
