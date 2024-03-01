package de.team33.patterns.generics.atlas;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.util.List;

import static java.util.Collections.singletonList;

class GenericArrayTypedef extends ArrayTypedef {

    private final Typedef componentType;

    GenericArrayTypedef(final GenericArrayType type, final Typedef context) {
        this.componentType = TypeCase.toTypedef(type.getGenericComponentType(), context);
    }

    private static Class<?> arrayClass(final Class<?> componentClass) {
        return Array.newInstance(componentClass, 0).getClass();
    }

    @Override
    final Class<?> asClass() {
        return arrayClass(componentType.asClass());
    }

    @Override
    final List<Typedef> getActualParameters() {
        return singletonList(componentType);
    }
}
