package de.team33.patterns.typing.atlas;

import java.util.List;

import static java.util.Collections.emptyList;

class ClassTypedef extends SingleTypedef {

    private final Class<?> underlyingClass;

    ClassTypedef(final Class<?> underlyingClass) {
        this.underlyingClass = underlyingClass;
    }

    @Override
    public final Class<?> asClass() {
        return underlyingClass;
    }

    @Override
    public final List<Typedef> getActualParameters() {
        return emptyList();
    }
}
