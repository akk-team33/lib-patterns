package de.team33.patterns.typing.atlas;

import java.util.function.Function;

enum ClassCase {

    CLASS(ClassDType::new),
    ARRAY(PlainArrayDType::new);

    private final Function<Class<?>, DType> mapping;

    ClassCase(final Function<Class<?>, DType> mapping) {
        this.mapping = mapping;
    }

    static DType toTypedef(final Class<?> underlyingClass) {
        return (underlyingClass.isArray() ? ARRAY : CLASS).mapping.apply(underlyingClass);
    }
}
