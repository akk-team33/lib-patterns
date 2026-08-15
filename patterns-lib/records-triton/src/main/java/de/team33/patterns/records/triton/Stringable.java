package de.team33.patterns.records.triton;

import de.team33.patterns.exceptional.dione.XFunction;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

final class Stringable {

    @SuppressWarnings("rawtypes")
    private static final Map<Class, UnaryOperator> SETUPS =
            new ConcurrentHashMap<>();

    @SuppressWarnings("rawtypes")
    private static final Map<Class, Mapper> MAPPERS =
            new ConcurrentHashMap<>();

    private static final List<Class<?>> STRING_CLASSES =
            List.of(CharSequence.class, String.class);
    private static final Comparator<Class<?>> CLASS_ORDER =
            Comparator.comparing(STRING_CLASSES::indexOf);
    private static final Comparator<Constructor<?>> CNSTRCTR_ORDER =
            Comparator.comparing(Stringable::cntrctrPrmtrClass, CLASS_ORDER);
    private static final Comparator<Method> METHOD_ORDER =
            Comparator.comparing(Stringable::methodPrmtrClass, CLASS_ORDER)
                      .thenComparing(Method::getName, String::compareToIgnoreCase)
                      .thenComparing(Method::getName, String::compareTo);

    private Stringable() {
    }

    static <T> T decode(final Class<T> type, final String parameter) {
        return mapper(type).reverse().map(parameter);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    static <T> String encode(final T source) {
        return (String) mapper((Class) source.getClass()).map(source);
    }

    static boolean isUntouched(final Class<?> type) {
        return null == MAPPERS.get(type);
    }

    @SuppressWarnings("BoundedWildcard")
    static <T> void setup(final Class<T> type,
                          final BiFunction<
                                  Class<T>,
                                  UnaryOperator<Mapping<T, String>>,
                                  UnaryOperator<Mapping<T, String>>> remapping) {
        SETUPS.compute(type, remapping::apply);
    }

    static boolean supports(final Class<?> type) {
        return mapper(type).isFullFeatured();
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

    @SuppressWarnings("unchecked")
    private static <T> UnaryOperator<Mapper<T, String>> setup(final Class<T> type) {
        return Optional.ofNullable(SETUPS.get(type))
                       .orElseGet(UnaryOperator::identity);
    }

    @SuppressWarnings("unchecked")
    private static <T> Mapper<T, String> mapper(final Class<T> type) {
        return MAPPERS.computeIfAbsent(type, Stringable::newMapper);
    }

    private static <T> Mapper<T, String> newMapper(final Class<T> type) {
        final UnaryOperator<Mapper<T, String>> setup = setup(type);
        return setup.apply(new Mapper<>(Object::toString, newMethod(type)));
    }

    private static <T> XFunction<String, T, Exception> newMethod(final Class<T> type) {
        return STRING_CLASSES.stream()
                             .map(stringClass -> constructor(type, stringClass))
                             .filter(Objects::nonNull)
                             .filter(cnstrctr -> Modifier.isPublic(cnstrctr.getModifiers()))
                             .max(CNSTRCTR_ORDER)
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
}
