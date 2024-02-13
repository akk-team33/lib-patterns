package de.team33.patterns.generics.atlas;

import java.lang.reflect.TypeVariable;
import java.util.List;
import java.util.Optional;

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
