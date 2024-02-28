package de.team33.patterns.typing.atlas;

import java.lang.reflect.TypeVariable;
import java.util.List;

class TypeVariableType extends SingleType {

    private final Type definite;

    TypeVariableType(final TypeVariable<?> type, final Type context) {
        this.definite = context.getActualParameter(type.getName());
    }

    @Override
    public final Class<?> asClass() {
        return definite.asClass();
    }

    @Override
    public final List<Type> getActualParameters() {
        return definite.getActualParameters();
    }
}
