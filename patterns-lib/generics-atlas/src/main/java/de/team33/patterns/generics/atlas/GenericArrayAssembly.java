package de.team33.patterns.generics.atlas;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.util.List;

import static java.util.Collections.singletonList;

class GenericArrayAssembly extends ArrayAssembly {

    private final Assembly componentType;

    GenericArrayAssembly(final GenericArrayType type, final Assembly context) {
        this.componentType = (TypeCase.toAssembly(type.getGenericComponentType(), context));
    }

    private static Class<?> arrayClass(final Class<?> componentClass) {
        return Array.newInstance(componentClass, 0).getClass();
    }

    @Override
    final Class<?> asClass() {
        return arrayClass(componentType.asClass());
    }

    @Override
    final List<Assembly> getActualParameters() {
        return singletonList(componentType);
    }
}
