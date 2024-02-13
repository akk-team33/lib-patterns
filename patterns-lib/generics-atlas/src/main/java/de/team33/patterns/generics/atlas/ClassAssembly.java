package de.team33.patterns.generics.atlas;

import java.util.List;

import static java.util.Collections.emptyList;

class ClassAssembly extends SingleAssembly {

    private final Class<?> underlyingClass;

    ClassAssembly(final Class<?> underlyingClass) {
        this.underlyingClass = underlyingClass;
    }

    @Override
    final Class<?> asClass() {
        return underlyingClass;
    }

    @Override
    final List<Assembly> getActualParameters() {
        return emptyList();
    }
}
