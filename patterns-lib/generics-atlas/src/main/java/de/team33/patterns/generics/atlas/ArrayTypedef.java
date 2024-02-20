package de.team33.patterns.generics.atlas;

import java.util.Collections;
import java.util.List;

abstract class ArrayTypedef extends Typedef {

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
