package de.team33.patterns.typing.atlas;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class ParameterizedDType extends SingleDType {

    private final ParameterizedType type;
    private final DType context;

    ParameterizedDType(final ParameterizedType type, final DType context) {
        this.type = type;
        this.context = context;
    }

    @Override
    public final Class<?> asClass() {
        return (Class<?>) type.getRawType();
    }

    @Override
    public final List<DType> getActualParameters() {
        return Stream.of(type.getActualTypeArguments())
                     .map(type1 -> TypeCase.toTypedef(type1, context))
                     .collect(Collectors.toList());
    }
}
