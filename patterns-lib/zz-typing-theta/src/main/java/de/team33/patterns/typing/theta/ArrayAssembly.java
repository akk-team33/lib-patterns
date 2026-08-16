package de.team33.patterns.typing.theta;

import java.util.Collections;
import java.util.List;

abstract class ArrayAssembly extends Assembly {

    private static final List<String> FORMAL_PARAMETERS = Collections.singletonList("E");

    @Override
    final List<String> getFormalParameters() {
        return FORMAL_PARAMETERS;
    }

    @Override
    final String toStringValue() {
        return getActualParameters().get(0) + "[]";
    }
}
