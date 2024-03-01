package de.team33.patterns.generics.atlas;

import de.team33.patterns.lazy.narvi.Lazy;

import java.util.Collections;
import java.util.List;

abstract class ArrayTypedef extends Typedef {

    private final transient Lazy<String> stringValue = Lazy.init(this::toStringValue);

    private static final List<String> FORMAL_PARAMETERS = Collections.singletonList("E");

    @SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
    @Override
    final List<String> getFormalParameters() {
        return FORMAL_PARAMETERS;
    }

    private String toStringValue() {
        return getActualParameters().get(0) + "[]";
    }

    @Override
    public final String toString() {
        return stringValue.get();
    }
}
