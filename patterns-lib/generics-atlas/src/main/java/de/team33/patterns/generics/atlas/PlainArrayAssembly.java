package de.team33.patterns.generics.atlas;

import java.util.Collections;
import java.util.List;

class PlainArrayAssembly extends ArrayAssembly {

    private final Class<?> underlyingClass;

    PlainArrayAssembly(final Class<?> underlyingClass) {
        this.underlyingClass = underlyingClass;
    }

    @Override
    final Class<?> asClass() {
        return underlyingClass;
    }

    @Override
    final List<Assembly> getActualParameters() {
        return Collections.singletonList(ClassCase.toAssembly(underlyingClass.getComponentType()));
    }
}
