package de.team33.patterns.typing.theta;

import java.lang.reflect.TypeVariable;
import java.util.List;

class TypeVariableSupport extends SingleSupport {

    private final Class<?> core;
    private final List<TypeSupport> actualParameters;

    TypeVariableSupport(final TypeVariable<?> type, final TypeSupport context) {
        final TypeSupport definite = context.actualParameter(type.getName());
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
