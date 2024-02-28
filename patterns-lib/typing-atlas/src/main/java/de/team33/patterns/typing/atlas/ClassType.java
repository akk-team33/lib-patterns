package de.team33.patterns.typing.atlas;

import java.util.List;

import static java.util.Collections.emptyList;

class ClassType extends SingleType {

    private final Class<?> underlyingClass;

    ClassType(final Class<?> underlyingClass) {
        this.underlyingClass = underlyingClass;
    }

    @Override
    public final Class<?> asClass() {
        return underlyingClass;
    }

    @Override
    public final List<Type> getActualParameters() {
        return emptyList();
    }
}
