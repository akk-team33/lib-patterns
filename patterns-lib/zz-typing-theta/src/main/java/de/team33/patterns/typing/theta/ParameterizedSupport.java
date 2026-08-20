package de.team33.patterns.typing.theta;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.stream.Stream;

class ParameterizedSupport extends SingleSupport {

    private final ParameterizedType type;
    private final TypeSupport context;

    ParameterizedSupport(final ParameterizedType type, final TypeSupport context) {
        this.type = type;
        this.context = context;
    }

    @Override
    final Class<?> core() {
        return (Class<?>) type.getRawType();
    }

    @Override
    final List<TypeSupport> actualParameters() {
        return features().get(Key.ACTUAL_PARAMETERS,
                              () -> Stream.of(type.getActualTypeArguments())
                                          .map(typeArg -> TypeCase.support(typeArg, context))
                                          .toList());
    }
}
