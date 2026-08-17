package de.team33.patterns.typing.theta;

import java.util.Collections;
import java.util.List;

class PlainArrayBacking extends ArrayBacking {

    private final Class<?> underlyingClass;

    PlainArrayBacking(final Class<?> underlyingClass) {
        this.underlyingClass = underlyingClass;
    }

    @Override
    final Class<?> core() {
        return underlyingClass;
    }

    @Override
    final List<Backing> actualParameters() {
        return Collections.singletonList(ClassCase.toBacking(underlyingClass.getComponentType()));
    }
}
