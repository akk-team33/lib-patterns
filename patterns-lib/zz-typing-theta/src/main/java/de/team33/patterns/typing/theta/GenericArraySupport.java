package de.team33.patterns.typing.theta;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.util.List;

class GenericArraySupport extends ArraySupport {

    private final TypeSupport componentType;

    GenericArraySupport(final GenericArrayType type, final TypeSupport context) {
        this.componentType = TypeCase.support(type.getGenericComponentType(), context);
    }

    private static Class<?> arrayClass(final Class<?> componentClass) {
        return Array.newInstance(componentClass, 0).getClass();
    }

    @Override
    final Class<?> core() {
        return arrayClass(componentType.core());
    }

    @Override
    final List<TypeSupport> actualParameters() {
        return features().get(Key.ACTUAL_PARAMETERS, () -> List.of(componentType));

    }
}
