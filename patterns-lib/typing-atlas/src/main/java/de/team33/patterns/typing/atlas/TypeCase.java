package de.team33.patterns.typing.atlas;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Stream;

enum TypeCase {

    CLASS(Class.class, (type, context) -> ClassCase.toTypedef(type)),

    GENERIC_ARRAY(GenericArrayType.class, GenericArrayDType::new),

    PARAMETERIZED_TYPE(ParameterizedType.class, ParameterizedDType::new),

    TYPE_VARIABLE(TypeVariable.class, TypeVariableDType::new);

    private final Predicate<Type> matching;
    private final BiFunction<Type, DType, DType> mapping;

    <T extends Type> TypeCase(final Class<T> typeClass, final BiFunction<T, DType, DType> mapping) {
        this.matching = typeClass::isInstance;
        this.mapping = (t, u) -> mapping.apply(typeClass.cast(t), u);
    }

    static DType toTypedef(final Type type, final DType context) {
        return Stream.of(values())
                     .filter(typeType -> typeType.matching.test(type)).findAny()
                     .map(typeType -> typeType.mapping.apply(type, context))
                     .orElseThrow(() -> new IllegalArgumentException("Unknown type of Type: " + type.getClass()));
    }
}
