package de.team33.patterns.typing.theta;

import java.util.List;

class ClassSupport extends SingleSupport {

    private final Class<?> core;

    ClassSupport(final Class<?> core) {
        this.core = core;
    }

    @Override
    final Class<?> core() {
        return core;
    }

    @Override
    final List<TypeSupport> actualParameters() {
        return List.of();
    }
}
