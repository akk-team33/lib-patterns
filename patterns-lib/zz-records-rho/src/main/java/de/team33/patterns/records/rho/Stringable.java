package de.team33.patterns.records.rho;

import de.team33.patterns.exceptional.dione.XFunction;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

final class Stringable {

    @SuppressWarnings("rawtypes")
    private static final Map<Class, Mapping> MAPPINGS =
            new ConcurrentHashMap<>();

    private static final List<Class<?>> STRING_CLASSES =
            List.of(CharSequence.class, String.class);
    private static final Comparator<Class<?>> CLASS_ORDER =
            Comparator.comparing(STRING_CLASSES::indexOf);
    private static final Comparator<Constructor<?>> CONSTRUCTOR_ORDER =
            Comparator.comparing(Stringable::cntrctrPrmtrClass, CLASS_ORDER);
    private static final Comparator<Method> METHOD_ORDER =
            Comparator.comparing(Stringable::methodPrmtrClass, CLASS_ORDER)
                      .thenComparing(Method::getName, String::compareToIgnoreCase)
                      .thenComparing(Method::getName, String::compareTo);

    private Stringable() {
    }

    static <T> T decode(final Class<T> type, final String parameter) {
        return mapping(type).reverse().map(parameter);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    static <T> String encode(final T source) {
        return (String) mapping((Class) source.getClass()).map(source);
    }

    static boolean supports(final Class<?> type) {
        return mapping(type).isFeatured();
    }

    @SuppressWarnings("unchecked")
    static <T> void setup(final Class<T> type, final UnaryOperator<Mapping<T, String>> operator) {
        MAPPINGS.compute(type, (key, value) -> setup(key, value, operator));
    }

    private static <T> Mapping<T, String> setup(final Class<T> type,
                                                final Mapping<T, String> found,
                                                final UnaryOperator<Mapping<T, String>> operator) {
        if (null == found) {
            return Objects.requireNonNull(operator.apply(newMapping(type)), "operator returns null");
        } else {
            throw new IllegalStateException("A mapping already exists for " + type);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> Mapping<T, String> mapping(final Class<T> type) {
        return MAPPINGS.computeIfAbsent(type, Stringable::newMapping);
    }

    private static <T> Mapping<T, String> newMapping(final Class<T> type) {
        return new Mapping<>(Object::toString, newMethod(type));
    }

    private static <T> XFunction<String, T, Exception> newMethod(final Class<T> type) {
        return STRING_CLASSES.stream()
                             .map(stringClass -> constructor(type, stringClass))
                             .filter(Objects::nonNull)
                             .filter(constructor -> Modifier.isPublic(constructor.getModifiers()))
                             .max(CONSTRUCTOR_ORDER)
                             .map(Stringable::toMethod)
                             .orElseGet(() -> newStaticMethod(type));
    }

    private static <T> XFunction<String, T, Exception> newStaticMethod(final Class<T> type) {
        return Stream.of(type.getDeclaredMethods())
                     .filter(method -> Modifier.isStatic(method.getModifiers()))
                     .filter(method -> Modifier.isPublic(method.getModifiers()))
                     .filter(method -> type.equals(method.getReturnType()))
                     .filter(method -> method.getParameterCount() == 1)
                     .filter(method -> STRING_CLASSES.contains(method.getParameterTypes()[0]))
                     .max(METHOD_ORDER)
                     .map(Stringable::<T>toMethod)
                     .orElse(null);
    }

    @SuppressWarnings("BoundedWildcard")
    private static <T> XFunction<String, T, Exception> toMethod(final Constructor<T> constructor) {
        return constructor::newInstance;
    }

    @SuppressWarnings("unchecked")
    private static <T> XFunction<String, T, Exception> toMethod(final Method method) {
        return parameter -> (T) method.invoke(null, parameter);
    }

    private static Class<?> cntrctrPrmtrClass(final Constructor<?> constructor) {
        return constructor.getParameterTypes()[0];
    }

    private static Class<?> methodPrmtrClass(final Method method) {
        return method.getParameterTypes()[0];
    }

    @SuppressWarnings("ReturnOfNull")
    private static <T> Constructor<T> constructor(final Class<T> tClass, final Class<?> pClass) {
        try {
            return tClass.getDeclaredConstructor(pClass);
        } catch (final NoSuchMethodException ignored) {
            return null;
        }
    }
}
