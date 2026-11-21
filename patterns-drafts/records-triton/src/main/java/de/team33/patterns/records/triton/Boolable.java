package de.team33.patterns.records.triton;

import de.team33.patterns.typing.proteus.Type;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

final class Boolable {

    @SuppressWarnings({"rawtypes", "CloneableClassInSecureContext", "ClassExtendsConcreteCollection"})
    private static final Map<Type, Function> REVERSE = new HashMap<>() {{
        put(Type.of(boolean.class), Function.identity());
        put(Type.of(Boolean.class), Function.identity());
    }};

    private Boolable() {}

    static <T> T resolve(final Boolean value, final Type<T> targetType) {
        @SuppressWarnings("unchecked")
        final Function<Boolean, T> mapping = REVERSE.get(targetType);
        if (mapping != null) {
            return mapping.apply(value);
        } else {
            throw new IllegalArgumentException("Cannot resolve " + value + " to " + targetType);
        }
    }
}
