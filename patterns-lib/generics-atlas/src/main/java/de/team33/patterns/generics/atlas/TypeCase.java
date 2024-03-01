package de.team33.patterns.generics.atlas;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Stream;

enum TypeCase {

    CLASS(Class.class, (type, context) -> ClassCase.toTypedef(type)),

    GENERIC_ARRAY(GenericArrayType.class, GenericArrayTypedef::new),

    PARAMETERIZED_TYPE(ParameterizedType.class, ParameterizedTypedef::new),

    TYPE_VARIABLE(TypeVariable.class, TypeVariableTypedef::new);

    private final Predicate<Type> matching;
    private final BiFunction<Type, Typedef, Typedef> mapping;

    <T extends Type> TypeCase(final Class<T> typeClass, final BiFunction<T, Typedef, Typedef> mapping) {
        this.matching = typeClass::isInstance;
        this.mapping = (t, u) -> mapping.apply(typeClass.cast(t), u);
    }

    static Typedef toTypedef(final Type type, final Typedef context) {
        return Stream.of(values())
                     .filter(typeType -> typeType.matching.test(type)).findAny()
                     .map(typeType -> typeType.mapping.apply(type, context))
                     .orElseThrow(() -> new IllegalArgumentException("Unknown type of Type: " + type.getClass()));
    }
}
