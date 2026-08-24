package de.team33.patterns.typing.proteus;

import java.util.List;

class PlainArraySupport extends ArraySupport {

    private final Class<?> core;

    PlainArraySupport(final Class<?> core) {
        this.core = core;
    }

    @Override
    final Class<?> core() {
        return core;
    }

    @Override
    final List<TypeSupport> actualParameters() {
        return features().get(Key.ACTUAL_PARAMETERS,
                              () -> List.of(ClassCase.support(core.getComponentType())));
    }
}
