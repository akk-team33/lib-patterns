package de.team33.patterns.typing.proteus;

import java.util.List;

abstract class ArraySupport extends CoreSupport {

    private static final List<String> FORMAL_PARAMETERS = List.of("E");

    @Override
    final List<String> formalParameters() {
        return FORMAL_PARAMETERS;
    }

    @Override
    final boolean isParametersCompatible(final TypeSupport other) {
        return actualParameters().get(0).isAssignableFrom(other.actualParameters().get(0));
    }

    @Override
    public final String toString() {
        return features().get(Key.TO_STRING, () -> actualParameters().get(0) + "[]");
    }
}
