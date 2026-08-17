package de.team33.patterns.typing.theta;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Stream;

enum TypeCase {

    CLASS(Class.class, (type, context) -> ClassCase.toBacking(type)),

    GENERIC_ARRAY(GenericArrayType.class, GenericArrayBacking::new),

    PARAMETERIZED_TYPE(ParameterizedType.class, ParameterizedBacking::new),

    TYPE_VARIABLE(TypeVariable.class, TypeVariableBacking::new);

    private final Predicate<Type> matching;
    private final BiFunction<Type, Backing, Backing> mapping;

    <T extends Type> TypeCase(final Class<T> typeClass, final BiFunction<T, Backing, Backing> mapping) {
        this.matching = typeClass::isInstance;
        this.mapping = (t, u) -> mapping.apply(typeClass.cast(t), u);
    }

    static Backing toBacking(final Type type, final Backing context) {
        return Stream.of(values())
                     .filter(typeType -> typeType.matching.test(type)).findAny()
                     .map(typeType -> typeType.mapping.apply(type, context))
                     .orElseThrow(() -> new IllegalArgumentException("Unknown type of Type: " + type.getClass()));
    }
}
