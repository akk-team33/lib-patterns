package de.team33.patterns.typing.theta;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class ParameterizedBacking extends SingleBacking {

    private final ParameterizedType type;
    private final Backing context;

    ParameterizedBacking(final ParameterizedType type, final Backing context) {
        this.type = type;
        this.context = context;
    }

    @Override
    final Class<?> core() {
        return (Class<?>) type.getRawType();
    }

    @Override
    final List<Backing> actualParameters() {
        return Stream.of(type.getActualTypeArguments())
                     .map(type1 -> TypeCase.toAssembly(type1, context))
                     .collect(Collectors.toList());
    }
}
