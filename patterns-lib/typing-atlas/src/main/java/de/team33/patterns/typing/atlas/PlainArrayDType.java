package de.team33.patterns.typing.atlas;

import java.util.Collections;
import java.util.List;

class PlainArrayDType extends ArrayDType {

    private final Class<?> underlyingClass;

    PlainArrayDType(final Class<?> underlyingClass) {
        this.underlyingClass = underlyingClass;
    }

    @Override
    public final Class<?> asClass() {
        return underlyingClass;
    }

    @Override
    public final List<DType> getActualParameters() {
        return Collections.singletonList(ClassCase.toTypedef(underlyingClass.getComponentType()));
    }
}
