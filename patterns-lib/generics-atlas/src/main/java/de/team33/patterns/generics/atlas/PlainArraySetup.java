package de.team33.patterns.generics.atlas;

import java.util.Collections;
import java.util.List;

class PlainArraySetup extends ArraySetup {

    private final Class<?> underlyingClass;

    PlainArraySetup(final Class<?> underlyingClass) {
        this.underlyingClass = underlyingClass;
    }

    @Override
    final Class<?> asClass() {
        return underlyingClass;
    }

    @Override
    final List<Setup> getActualParameters() {
        return Collections.singletonList(ClassCase.toStage(underlyingClass.getComponentType()));
    }
}
