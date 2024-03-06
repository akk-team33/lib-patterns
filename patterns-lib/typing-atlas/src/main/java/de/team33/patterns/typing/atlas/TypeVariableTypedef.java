package de.team33.patterns.typing.atlas;

import java.lang.reflect.TypeVariable;
import java.util.List;

class TypeVariableTypedef extends SingleTypedef {

    private final Typedef definite;

    TypeVariableTypedef(final TypeVariable<?> type, final Typedef context) {
        this.definite = context.getActualParameter(type.getName());
    }

    @Override
    final Class<?> asClass() {
        return definite.asClass();
    }

    @Override
    final List<? extends Typedef> getActualParameters() {
        return definite.getActualParameters();
    }
}