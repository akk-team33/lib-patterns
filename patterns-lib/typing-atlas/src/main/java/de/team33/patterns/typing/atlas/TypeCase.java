package de.team33.patterns.typing.atlas;

import java.lang.reflect.TypeVariable;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Stream;

enum TypeCase {

    CLASS(Class.class, (type, context) -> ClassCase.toTypedef(type)),

    GENERIC_ARRAY(java.lang.reflect.GenericArrayType.class, GenericArrayType::new),

    PARAMETERIZED_TYPE(java.lang.reflect.ParameterizedType.class, ParameterizedType::new),

    TYPE_VARIABLE(TypeVariable.class, TypeVariableType::new);

    private final Predicate<java.lang.reflect.Type> matching;
    private final BiFunction<java.lang.reflect.Type, Type, Type> mapping;

    <T extends java.lang.reflect.Type> TypeCase(final Class<T> typeClass, final BiFunction<T, Type, Type> mapping) {
        this.matching = typeClass::isInstance;
        this.mapping = (t, u) -> mapping.apply(typeClass.cast(t), u);
    }

    static Type toTypedef(final java.lang.reflect.Type type, final Type context) {
        return Stream.of(values())
                     .filter(typeType -> typeType.matching.test(type)).findAny()
                     .map(typeType -> typeType.mapping.apply(type, context))
                     .orElseThrow(() -> new IllegalArgumentException("Unknown type of Type: " + type.getClass()));
    }
}
