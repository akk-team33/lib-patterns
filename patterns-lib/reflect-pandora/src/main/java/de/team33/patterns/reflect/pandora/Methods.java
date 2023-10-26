package de.team33.patterns.reflect.pandora;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Locale;
import java.util.stream.Stream;

final class Methods {

    private static final int SYNTHETIC = 0x00001000;
    private static final int NON_INSTANCE = Modifier.STATIC | Modifier.NATIVE | SYNTHETIC;

    private Methods() {
    }

    static Stream<Method> publicGetters(final Class<?> subjectClass) {
        return Methods.significantMethods(subjectClass)
                      .filter(Methods::isGetterPrefix)
                      .filter(Methods::isGetterParameters)
                      .filter(method -> isGetterResult(method, subjectClass));
    }

    static Stream<Method> publicSetters(final Class<?> subjectClass) {
        return Methods.significantMethods(subjectClass)
                      .filter(Methods::isSetterPrefix)
                      .filter(Methods::isSetterParameters)
                      .filter(method -> isSetterResult(method, subjectClass));
    }

    private static Stream<Method> significantMethods(final Class<?> subjectClass) {
        return Stream.of(subjectClass.getMethods())
                     .filter(method -> !Object.class.equals(method.getDeclaringClass()))
                     .filter(Methods::isInstance);
    }

    private static boolean isGetterPrefix(final Method method) {
        return Type.GETTER == Prefix.of(method.getName()).type;
    }

    private static boolean isSetterPrefix(final Method method) {
        return Type.SETTER == Prefix.of(method.getName()).type;
    }

    private static boolean isGetterParameters(final Method method) {
        return 0 == method.getParameterCount();
    }

    private static boolean isSetterParameters(final Method method) {
        return 1 == method.getParameterCount();
    }

    private static boolean isGetterResult(final Method method, final Class<?> subjectClass) {
        return !isSetterResult(method, subjectClass);
    }

    private static boolean isSetterResult(final Method method, final Class<?> subjectClass) {
        final Class<?> returnType = method.getReturnType();
        return void.class.equals(returnType)
               || Void.class.equals(returnType)
               || subjectClass.equals(returnType) /* TODO? ...
               || method.getDeclaringClass().equals(returnType)*/;
    }

    private static boolean isInstance(final Method method) {
        return isInstance(method.getModifiers());
    }

    private static boolean isInstance(final int modifiers) {
        return 0 == (modifiers & NON_INSTANCE);
    }

    static String normalName(final Method method) {
        final String name = method.getName();
        final Prefix prefix = Prefix.of(name);
        final int headIndex = prefix.length;
        final int tailIndex = (headIndex < name.length()) ? (headIndex + 1) : headIndex;
        final String head = name.substring(headIndex, tailIndex).toLowerCase(Locale.ROOT);
        return head + name.substring(tailIndex);
    }

    private enum Type {

        SETTER,
        GETTER,
        OTHER
    }

    private enum Prefix {

        is(Type.GETTER),
        get(Type.GETTER),
        set(Type.SETTER),
        NONE(Type.OTHER, 0);

        final Type type;
        final int length;

        Prefix(final Type type, final int length) {
            this.type = type;
            this.length = length;
        }

        Prefix(final Type type) {
            this.type = type;
            this.length = name().length();
        }

        static Prefix of(final String name) {
            return Stream.of(Prefix.values())
                         .filter(pfx -> name.startsWith(pfx.name()))
                         .findAny()
                         .orElse(Prefix.NONE);
        }
    }
}
