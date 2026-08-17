package de.team33.patterns.typing.theta;

import java.util.Collections;
import java.util.List;

abstract class ArrayBacking extends Backing {

    private static final List<String> FORMAL_PARAMETERS = Collections.singletonList("E");

    @Override
    final List<String> formalParameters() {
        return FORMAL_PARAMETERS;
    }

    @Override
    final String toStringValue() {
        return actualParameters().get(0) + "[]";
    }
}
