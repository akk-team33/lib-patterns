package de.team33.patterns.typing.atlas;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class ParameterizedType extends SingleType {

    private final java.lang.reflect.ParameterizedType type;
    private final Type context;

    ParameterizedType(final java.lang.reflect.ParameterizedType type, final Type context) {
        this.type = type;
        this.context = context;
    }

    @Override
    public final Class<?> asClass() {
        return (Class<?>) type.getRawType();
    }

    @Override
    public final List<Type> getActualParameters() {
        return Stream.of(type.getActualTypeArguments())
                     .map(type1 -> TypeCase.toTypedef(type1, context))
                     .collect(Collectors.toList());
    }
}
