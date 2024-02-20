package de.team33.patterns.generics.atlas;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class ParameterizedTypedef extends SingleTypedef {

    private final ParameterizedType type;
    private final Typedef context;

    ParameterizedTypedef(final ParameterizedType type, final Typedef context) {
        this.type = type;
        this.context = context;
    }

    @Override
    final Class<?> asClass() {
        return (Class<?>) type.getRawType();
    }

    @Override
    final List<Typedef> getActualParameters() {
        return Stream.of(type.getActualTypeArguments())
                     .map(type1 -> TypeCase.toAssembly(type1, context))
                     .collect(Collectors.toList());
    }
}
