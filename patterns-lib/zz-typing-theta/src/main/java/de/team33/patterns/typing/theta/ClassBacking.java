package de.team33.patterns.typing.theta;

import java.util.List;

import static java.util.Collections.emptyList;

class ClassBacking extends SingleBacking {

    private final Class<?> underlyingClass;

    ClassBacking(final Class<?> underlyingClass) {
        this.underlyingClass = underlyingClass;
    }

    @Override
    final Class<?> core() {
        return underlyingClass;
    }

    @Override
    final List<Backing> actualParameters() {
        return emptyList();
    }
}
