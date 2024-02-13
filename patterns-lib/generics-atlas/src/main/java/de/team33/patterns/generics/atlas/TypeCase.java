package de.team33.patterns.generics.atlas;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Stream;

enum TypeCase {

    CLASS(Class.class, (type, context) -> ClassCase.toAssembly(type)),

    GENERIC_ARRAY(GenericArrayType.class, GenericArrayAssembly::new),

    PARAMETERIZED_TYPE(ParameterizedType.class, ParameterizedAssembly::new),

    TYPE_VARIABLE(TypeVariable.class, TypeVariableAssembly::new);

    private final Predicate<Type> matching;
    private final BiFunction<Type, Assembly, Assembly> mapping;

    <T extends Type> TypeCase(final Class<T> typeClass, final BiFunction<T, Assembly, Assembly> mapping) {
        this.matching = typeClass::isInstance;
        this.mapping = (t, u) -> mapping.apply(typeClass.cast(t), u);
    }

    static Assembly toAssembly(final Type type, final Assembly context) {
        return Stream.of(values())
                     .filter(typeType -> typeType.matching.test(type)).findAny()
                     .map(typeType -> typeType.mapping.apply(type, context))
                     .orElseThrow(() -> new IllegalArgumentException("Unknown type of Type: " + type.getClass()));
    }
}
