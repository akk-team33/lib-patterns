package de.team33.patterns.typing.theta;

import java.util.function.Function;

enum ClassCase {

    CLASS(ClassBacking::new),
    ARRAY(PlainArrayBacking::new);

    private final Function<Class<?>, Backing> mapping;

    ClassCase(final Function<Class<?>, Backing> mapping) {
        this.mapping = mapping;
    }

    static Backing toBacking(final Class<?> underlyingClass) {
        return (underlyingClass.isArray() ? ARRAY : CLASS).mapping.apply(underlyingClass);
    }
}
