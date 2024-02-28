package de.team33.patterns.typing.atlas;

import java.lang.reflect.TypeVariable;
import java.util.List;

class TypeVariableDType extends SingleDType {

    private final DType definite;

    TypeVariableDType(final TypeVariable<?> type, final DType context) {
        this.definite = context.getActualParameter(type.getName());
    }

    @Override
    public final Class<?> asClass() {
        return definite.asClass();
    }

    @Override
    public final List<DType> getActualParameters() {
        return definite.getActualParameters();
    }
}
