package de.team33.patterns.typing.theta;

import java.util.List;

abstract class ArraySupport extends TypeSupport {

    private static final List<String> FORMAL_PARAMETERS = List.of("E");

    @Override
    final List<String> formalParameters() {
        return FORMAL_PARAMETERS;
    }

    @Override
    public final String toString() {
        return features().get(Key.TO_STRING, () -> actualParameters().get(0) + "[]");
    }
}
