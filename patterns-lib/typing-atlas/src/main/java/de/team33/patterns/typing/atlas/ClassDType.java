package de.team33.patterns.typing.atlas;

import java.util.List;

import static java.util.Collections.emptyList;

class ClassDType extends SingleDType {

    private final Class<?> underlyingClass;

    ClassDType(final Class<?> underlyingClass) {
        this.underlyingClass = underlyingClass;
    }

    @Override
    public final Class<?> asClass() {
        return underlyingClass;
    }

    @Override
    public final List<DType> getActualParameters() {
        return emptyList();
    }
}
