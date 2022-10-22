package de.team33.patterns.random.tarvos;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Locale;
import java.util.stream.Stream;

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

    static String normalName(final Method method) {
        final String name = method.getName();
        final Prefix prefix = Stream.of(Prefix.values())
                                    .filter(pfx -> name.startsWith(pfx.value))
                                    .findAny()
                                    .orElse(Prefix.NONE);
        final int headIndex = prefix.value.length();
        final int tailIndex = (headIndex < name.length()) ? (headIndex + 1) : headIndex;
        final String head = name.substring(headIndex, tailIndex).toUpperCase(Locale.ROOT);
        return head + name.substring(tailIndex);
    }

    private enum Prefix {

        NEXT,
        ANY,
        GET,
        NEW,
        SET,
        IS,
        TO,
        NONE("");

        final String value;

        Prefix(final String value) {
            this.value = value;
        }

        Prefix() {
            this.value = name().toLowerCase(Locale.ROOT);
        }
    }
}
