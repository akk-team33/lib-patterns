package de.team33.patterns.typing.atlas;

import java.util.Collections;
import java.util.List;

class PlainArrayType extends ArrayType {

    private final Class<?> underlyingClass;

    PlainArrayType(final Class<?> underlyingClass) {
        this.underlyingClass = underlyingClass;
    }

    @Override
    public final Class<?> asClass() {
        return underlyingClass;
    }

    @Override
    public final List<Type> getActualParameters() {
        return Collections.singletonList(ClassCase.toTypedef(underlyingClass.getComponentType()));
    }
}
