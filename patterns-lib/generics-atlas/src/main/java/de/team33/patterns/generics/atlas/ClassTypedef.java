package de.team33.patterns.generics.atlas;

import java.util.List;

import static java.util.Collections.emptyList;

class ClassTypedef extends SingleTypedef {

    private final Class<?> underlyingClass;

    ClassTypedef(final Class<?> underlyingClass) {
        this.underlyingClass = underlyingClass;
    }

    @Override
    final Class<?> asClass() {
        return underlyingClass;
    }

    @Override
    final List<Typedef> getActualParameters() {
        return emptyList();
    }
}
