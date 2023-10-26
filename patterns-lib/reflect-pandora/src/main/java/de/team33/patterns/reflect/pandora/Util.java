package de.team33.patterns.reflect.pandora;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableSet;

final class Util {

    private static final int SYNTHETIC = 0x00001000;
    private static final int NON_INSTANCE = Modifier.STATIC | Modifier.NATIVE | SYNTHETIC;

    static final Set<Class<?>> VOID_TYPES = unmodifiableSet(new HashSet<>(asList(void.class, Void.class)));

    static Stream<Method> instanceMethods(final Class<?> subjectClass) {
        return Stream.of(subjectClass.getMethods())
                     .filter(Util::isNoObjectMethod)
                     .filter(Util::isInstanceMethod);
    }

    private static boolean isInstanceMethod(final Method method) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    static String normal(final String name) {
        return name.isEmpty() ? name : name.substring(0, 1).toLowerCase() + name.substring(1);
    }

    static boolean isNoObjectMethod(final Method method) {
        return Object.class.equals(method.getDeclaringClass());
    }

    static boolean isGetter(final Method method) {
        return (0 == method.getParameterCount()) && (!Void.class.equals(method.getReturnType()));
    }

    static boolean isSetter(final Method method) {
        return (1 == method.getParameterCount()) && isReturnVoidOrSelf(method);
    }

    private static boolean isReturnVoidOrSelf(Method method) {
        final Class<?> returnType = method.getReturnType();
        return Void.class.equals(returnType) || method.getDeclaringClass().equals(returnType);
    }
}
