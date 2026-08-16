package de.team33.patterns.typing.theta;

import java.lang.reflect.TypeVariable;
import java.util.List;

class TypeVariableAssembly extends SingleAssembly {

    private final Assembly definite;

    TypeVariableAssembly(final TypeVariable<?> type, final Assembly context) {
        this.definite = context.getActualParameter(type.getName());
    }

    @Override
    final Class<?> asClass() {
        return definite.asClass();
    }

    @Override
    final List<Assembly> getActualParameters() {
        return definite.getActualParameters();
    }
}
