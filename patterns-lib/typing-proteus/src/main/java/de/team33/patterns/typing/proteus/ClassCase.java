package de.team33.patterns.typing.proteus;

import java.util.function.Function;

enum ClassCase {

    CLASS(ClassSupport::new),
    ARRAY(PlainArraySupport::new);

    private final Function<Class<?>, TypeSupport> mapping;

    ClassCase(final Function<Class<?>, TypeSupport> mapping) {
        this.mapping = mapping;
    }

    static TypeSupport support(final Class<?> underlyingClass) {
        return (underlyingClass.isArray() ? ARRAY : CLASS).mapping.apply(underlyingClass);
    }
}
