package de.team33.patterns.typing.atlas;

import java.lang.reflect.Array;
import java.util.List;

import static java.util.Collections.singletonList;

class GenericArrayType extends ArrayType {

    private final Type componentType;

    GenericArrayType(final java.lang.reflect.GenericArrayType type, final Type context) {
        this.componentType = TypeCase.toTypedef(type.getGenericComponentType(), context);
    }

    private static Class<?> arrayClass(final Class<?> componentClass) {
        return Array.newInstance(componentClass, 0).getClass();
    }

    @Override
    public final Class<?> asClass() {
        return arrayClass(componentType.asClass());
    }

    @Override
    public final List<Type> getActualParameters() {
        return singletonList(componentType);
    }
}
