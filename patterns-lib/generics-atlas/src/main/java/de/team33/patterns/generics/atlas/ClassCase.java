package de.team33.patterns.generics.atlas;

import java.util.function.Function;

enum ClassCase {

    CLASS(ClassAssembly::new),
    ARRAY(PlainArrayAssembly::new);

    private final Function<Class<?>, Assembly> mapping;

    ClassCase(final Function<Class<?>, Assembly> mapping) {
        this.mapping = mapping;
    }

    static Assembly toAssembly(final Class<?> underlyingClass) {
        return (underlyingClass.isArray() ? ARRAY : CLASS).mapping.apply(underlyingClass);
    }
}
