package de.team33.patterns.typing.theta;

import java.lang.reflect.*;
import java.lang.reflect.Type;
import java.util.function.Predicate;
import java.util.stream.Stream;

enum TypeCase {

    CLASS(Class.class, (type, context) -> ClassCase.support(type)),
    GENERIC_ARRAY(GenericArrayType.class, GenericArraySupport::new),
    PARAMETERIZED_TYPE(ParameterizedType.class, ParameterizedSupport::new),
    TYPE_VARIABLE(TypeVariable.class, TypeVariableSupport::new),
    WILDCARD_TYPE(WildcardType.class, WildcardSupport::new);

    private final Predicate<Type> matching;
    private final Mapping<Type> mapping;

    <T extends Type> TypeCase(final Class<T> typeClass, final Mapping<T> mapping) {
        this.matching = typeClass::isInstance;
        this.mapping = mapping::apply;
    }

    static TypeSupport support(final Type type, final TypeSupport context) {
        return Stream.of(values())
                     .filter(typeType -> typeType.matching.test(type)).findAny()
                     .map(typeType -> typeType.mapping.apply(type, context))
                     .orElseThrow(() -> new IllegalArgumentException("Unknown type of Type: " + type.getClass()));
    }

    @FunctionalInterface
    private interface Mapping<T extends Type> {

        TypeSupport applyT(T type, TypeSupport context);

        @SuppressWarnings("unchecked")
        default TypeSupport apply(final Type type, final TypeSupport context) {
            return applyT((T) type, context);
        }
    }
}
