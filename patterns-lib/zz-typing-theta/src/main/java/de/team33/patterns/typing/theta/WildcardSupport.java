package de.team33.patterns.typing.theta;

import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.List;

class WildcardSupport extends SingleSupport {

    private final Class<?> core;
    private final List<TypeSupport> actualParameters;

    WildcardSupport(final WildcardType type, final TypeSupport context) {
        final Type[] lowerBounds = type.getLowerBounds();
        final Type[] upperBounds = type.getUpperBounds();
        final String typeName = type.getTypeName();
        final TypeSupport definite = context; //context.actualParameter(type.getName());
        this.core = definite.core();
        this.actualParameters = definite.actualParameters();
    }

    @Override
    final Class<?> core() {
        return core;
    }

    @Override
    final List<TypeSupport> actualParameters() {
        // already is immutable ...
        // noinspection AssignmentOrReturnOfFieldWithMutableType
        return actualParameters;
    }
}
