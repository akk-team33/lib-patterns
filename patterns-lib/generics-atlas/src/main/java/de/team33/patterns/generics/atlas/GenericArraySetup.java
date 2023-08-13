package de.team33.patterns.generics.atlas;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.util.List;

import static java.util.Collections.singletonList;

class GenericArraySetup extends ArraySetup {

    private final Setup componentType;

    GenericArraySetup(final GenericArrayType type, final Setup context) {
        this.componentType = (TypeCase.toStage(type.getGenericComponentType(), context));
    }

    private static Class<?> arrayClass(final Class<?> componentClass) {
        return Array.newInstance(componentClass, 0).getClass();
    }

    @Override
    final Class<?> asClass() {
        return arrayClass(componentType.asClass());
    }

    @Override
    final List<Setup> getActualParameters() {
        return singletonList(componentType);
    }
}
