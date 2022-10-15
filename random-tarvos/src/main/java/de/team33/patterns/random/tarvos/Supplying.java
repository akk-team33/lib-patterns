package de.team33.patterns.random.tarvos;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class Supplying {

    private static final Map<Class<?>, List<Method>> SUPPLIERS = new ConcurrentHashMap<>(0);
    private static final Predicate<Object> ANY_METHOD = method -> true;

    private final List<Method> suppliers;

    Supplying(final Class<?> sourceType) {
        this.suppliers = SUPPLIERS.computeIfAbsent(sourceType, Supplying::suppliersOf);
    }

    private static List<Method> suppliersOf(final Class<?> sourceType) {
        return Stream.of(sourceType.getMethods())
                     .filter(method -> !Object.class.equals(method.getDeclaringClass()))
                     .filter(Methods::isSupplier)
                     .collect(Collectors.toList());
    }

    final Method desiredSupplier(final Type resultType) {
        return desiredSupplier(resultType, ANY_METHOD);
    }

    final Method desiredSupplier(final Type resultType, final Predicate<? super Method> desired) {
        return suppliers.stream()
                        .filter(method -> resultType.equals(method.getGenericReturnType()))
                        .filter(desired)
                        .findAny()
                        .orElse(null);
    }
}
