package de.team33.patterns.random.tarvos;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableSet;

@Deprecated
final class Methods {

    private static final int SYNTHETIC = 0x00001000;
    private static final int NON_INSTANCE = Modifier.STATIC | Modifier.NATIVE | SYNTHETIC;
    private static final Set<String> IGNORABLE = unmodifiableSet(new TreeSet<>(asList(
            "hashCode",
            "toString"
    )));

    private Methods() {
    }

    static Stream<Method> publicGetters(final Class<?> targetClass) {
        return Stream.of(targetClass.getMethods())
                     .filter(Methods::isNotIgnorable)
                     .filter(Methods::isInstance)
                     .filter(Methods::isGetterParameters)
                     .filter(method -> isGetterResult(method, targetClass));
    }

    static Stream<Method> publicSetters(final Class<?> targetClass) {
        return Stream.of(targetClass.getMethods())
                     .filter(Methods::isInstance)
                     .filter(Methods::isSetterParameters)
                     .filter(method -> isSetterResult(method, targetClass));
    }

    private static boolean isGetterParameters(final Method method) {
        return 0 == method.getParameterCount();
    }

    private static boolean isSetterParameters(final Method method) {
        return 1 == method.getParameterCount();
    }

    private static boolean isGetterResult(final Method method, final Class<?> targetClass) {
        return !isSetterResult(method, targetClass);
    }

    private static boolean isSetterResult(final Method method, final Class<?> targetClass) {
        final Class<?> returnType = method.getReturnType();
        return void.class.equals(returnType)
                || targetClass.equals(returnType)
                || method.getDeclaringClass().equals(returnType);
    }

    private static boolean isNotIgnorable(final Method method) {
        return !IGNORABLE.contains(method.getName());
    }

    private static boolean isInstance(final Method method) {
        return isInstance(method.getModifiers());
    }

    private static boolean isInstance(final int modifiers) {
        return 0 == (modifiers & NON_INSTANCE);
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
        AS,
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
