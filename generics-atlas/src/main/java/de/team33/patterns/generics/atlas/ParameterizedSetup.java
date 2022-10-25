package de.team33.patterns.generics.atlas;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class ParameterizedSetup extends SingleSetup {

    private final ParameterizedType type;
    private final Setup context;

    ParameterizedSetup(final ParameterizedType type, final Setup context) {
        this.type = type;
        this.context = context;
    }

    @Override
    final Class<?> asClass() {
        return (Class<?>) type.getRawType();
    }

    @Override
    final List<Setup> getActualParameters() {
        return Stream.of(type.getActualTypeArguments())
                     .map(type1 -> TypeCase.toStage(type1, context))
                     .collect(Collectors.toList());
    }
}
