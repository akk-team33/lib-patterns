package de.team33.patterns.random.tarvos;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

final class Methods {

    private static final int SYNTHETIC = 0x00001000;
    private static final int NON_INSTANCE = Modifier.STATIC | Modifier.NATIVE | SYNTHETIC;

    private Methods() {
    }

    private static boolean isInstance(final Method method) {
        return isInstance(method.getModifiers());
    }

    private static boolean isInstance(final int modifiers) {
        return 0 == (modifiers & NON_INSTANCE);
    }

    static boolean isSetter(final Method method) {
        return isInstance(method) && method.getName().startsWith("set") && (1 == method.getParameterCount());
    }

    static boolean isSupplier(final Method method) {
        return isInstance(method) && (0 == method.getParameterCount());
    }
}
