package de.team33.patterns.generics.atlas;

import java.lang.reflect.TypeVariable;
import java.util.List;
import java.util.Optional;

class TypeVariableSetup extends SingleSetup {

    private final Setup definite;

    TypeVariableSetup(final TypeVariable<?> type, final Setup context) {
        final String name = type.getName();
        this.definite = Optional.ofNullable(context.getActualParameter(name))
                                .orElseThrow(() -> new IllegalArgumentException(
                                        String.format("Variable <%s> not found in parameters %s", name, context)));
    }

    @Override
    final Class<?> asClass() {
        return definite.asClass();
    }

    @Override
    final List<Setup> getActualParameters() {
        return definite.getActualParameters();
    }
}
