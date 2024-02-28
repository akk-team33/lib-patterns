package de.team33.patterns.typing.atlas;

import java.util.Collections;
import java.util.List;

class PlainArrayTypedef extends ArrayTypedef {

    private final Class<?> underlyingClass;

    PlainArrayTypedef(final Class<?> underlyingClass) {
        this.underlyingClass = underlyingClass;
    }

    @Override
    public final Class<?> asClass() {
        return underlyingClass;
    }

    @Override
    public final List<Typedef> getActualParameters() {
        return Collections.singletonList(ClassCase.toTypedef(underlyingClass.getComponentType()));
    }
}
