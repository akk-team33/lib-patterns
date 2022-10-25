package de.team33.patterns.generics.atlas;

import java.util.List;

import static java.util.Collections.emptyList;

class ClassSetup extends SingleSetup {

    private final Class<?> underlyingClass;

    ClassSetup(final Class<?> underlyingClass) {
        this.underlyingClass = underlyingClass;
    }

    @Override
    final Class<?> asClass() {
        return underlyingClass;
    }

    @Override
    final List<Setup> getActualParameters() {
        return emptyList();
    }
}
