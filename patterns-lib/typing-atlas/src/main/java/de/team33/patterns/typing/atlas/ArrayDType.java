package de.team33.patterns.typing.atlas;

import de.team33.patterns.lazy.narvi.Lazy;

import java.util.Collections;
import java.util.List;

abstract class ArrayDType extends DType {

    private final transient Lazy<String> stringValue = Lazy.init(this::toStringValue);

    private static final List<String> FORMAL_PARAMETERS = Collections.singletonList("E");

    @Override
    public final List<String> getFormalParameters() {
        // Field is an immutable List ...
        // noinspection AssignmentOrReturnOfFieldWithMutableType
        return FORMAL_PARAMETERS;
    }

    @Override
    public final String toString() {
        return stringValue.get();
    }

    private String toStringValue() {
        return getActualParameters().get(0) + "[]";
    }
}
