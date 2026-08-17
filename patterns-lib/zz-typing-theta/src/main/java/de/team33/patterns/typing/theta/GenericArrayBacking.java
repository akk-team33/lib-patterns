package de.team33.patterns.typing.theta;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.util.List;

import static java.util.Collections.singletonList;

class GenericArrayBacking extends ArrayBacking {

    private final Backing componentType;

    GenericArrayBacking(final GenericArrayType type, final Backing context) {
        this.componentType = (TypeCase.toAssembly(type.getGenericComponentType(), context));
    }

    private static Class<?> arrayClass(final Class<?> componentClass) {
        return Array.newInstance(componentClass, 0).getClass();
    }

    @Override
    final Class<?> core() {
        return arrayClass(componentType.core());
    }

    @Override
    final List<Backing> actualParameters() {
        return singletonList(componentType);
    }
}
