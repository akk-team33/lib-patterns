package de.team33.patterns.typing.theta;

import java.lang.reflect.TypeVariable;
import java.util.List;

class TypeVariableBacking extends SingleBacking {

    private final Backing definite;

    TypeVariableBacking(final TypeVariable<?> type, final Backing context) {
        this.definite = context.getActualParameter(type.getName());
    }

    @Override
    final Class<?> core() {
        return definite.core();
    }

    @Override
    final List<Backing> actualParameters() {
        return definite.actualParameters();
    }
}
