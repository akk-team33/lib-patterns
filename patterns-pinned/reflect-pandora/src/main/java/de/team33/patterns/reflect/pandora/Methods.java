package de.team33.patterns.reflect.pandora;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

final class Methods {

    private static final int SYNTHETIC = 0x00001000;
    private static final int NON_INHERENT_MODIFIERS = Modifier.STATIC | Modifier.NATIVE | SYNTHETIC;
    private static final Set<Signature> NON_INHERENT_SIGNATURES = publicOf(Object.class).map(Signature::new)
                                                                                        .collect(toSet());

    private Methods() {
    }

    static Stream<Method> publicOf(final Class<?> subjectClass) {
        return Stream.of(subjectClass.getMethods());
    }

    static Stream<Method> publicInherentOf(final Class<?> subjectClass) {
        return publicOf(subjectClass).filter(Methods::isInherent);
    }

    static Stream<Method> publicGettersOf(final Class<?> subjectClass) {
        return publicInherentOf(subjectClass).filter(Methods::isGetter);
    }

    static Stream<Method> publicSettersOf(final Class<?> subjectClass) {
        return publicInherentOf(subjectClass).filter(Methods::isSetter);
    }

    static Stream<Method> classicGettersOf(final Class<?> subjectClass) {
        return publicGettersOf(subjectClass).filter(Methods::isGetterPrefixed);
    }

    static Stream<Method> classicSettersOf(final Class<?> subjectClass) {
        return publicSettersOf(subjectClass).filter(Methods::isSetterPrefixed);
    }

    private static boolean isSetterPrefixed(final Method method) {
        return method.getName().startsWith(Prefix.set.name());
    }

    private static boolean isGetterPrefixed(final Method method) {
        return isGetterPrefixed(method.getName());
    }

    private static boolean isGetterPrefixed(final String name) {
        return name.startsWith(Prefix.get.name()) || name.startsWith(Prefix.is.name());
    }

    private static boolean isGetter(final Method method) {
        return (0 == method.getParameterCount()) && !isSetterResult(method);
    }

    private static boolean isSetter(final Method method) {
        return (1 == method.getParameterCount()) && isSetterResult(method);
    }

    private static boolean isSetterResult(final Method method) {
        final Class<?> returnType = method.getReturnType();
        return void.class.equals(returnType)
                || Void.class.equals(returnType)
                || method.getDeclaringClass().equals(returnType);
    }

    static String signatureOf(final Method method) {
        return new Signature(method).toString();
    }

    private static boolean isInherent(final Method method) {
        return !NON_INHERENT_SIGNATURES.contains(new Signature(method)) && isInherent(method.getModifiers());
    }

    private static boolean isInherent(final int modifiers) {
        return 0 == (modifiers & NON_INHERENT_MODIFIERS);
    }

    static String normalName(final Method method) {
        final String name = method.getName();
        final Prefix prefix = Prefix.of(name);
        final int headIndex = prefix.length;
        final int tailIndex = (headIndex < name.length()) ? (headIndex + 1) : headIndex;
        final String head = name.substring(headIndex, tailIndex).toLowerCase(Locale.ROOT);
        return head + name.substring(tailIndex);
    }

    private static class Signature {

        final String name;
        final List<Class<?>> parameterTypes;

        Signature(final Method method) {
            this.name = method.getName();
            this.parameterTypes = Arrays.asList(method.getParameterTypes());
        }

        private List<Object> toList() {
            return Arrays.asList(name, parameterTypes);
        }

        @Override
        public final int hashCode() {
            return toList().hashCode();
        }

        @Override
        public final boolean equals(final Object obj) {
            return (this == obj) || ((obj instanceof Signature) && toList().equals(((Signature) obj).toList()));
        }

        @Override
        public final String toString() {
            return String.format("%s(%s)", name, parameterTypes.stream()
                                                               .map(Class::getCanonicalName)
                                                               .collect(Collectors.joining(", ")));
        }
    }
}
