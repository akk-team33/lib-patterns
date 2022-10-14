package de.team33.patterns.random.tarvos;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class Suppliers {

    private static final Map<Class<?>, List<Method>> SUPPLIERS = new ConcurrentHashMap<>(0);

    final Object source;
    final Class<?> sourceType;
    private final Predicate<Method> desired;

    Suppliers(final Object source, final Collection<String> ignore) {
        this.source = source;
        this.sourceType = source.getClass();
        this.desired = nameFilter(new HashSet<>(ignore)).negate();
    }

    private static List<Method> newSuppliersOf(final Class<?> sourceType) {
        return Stream.of(sourceType.getMethods())
                     .filter(method -> !Object.class.equals(method.getDeclaringClass()))
                     .filter(Methods::isSupplier)
                     .collect(Collectors.toList());
    }

    private static Predicate<Method> nameFilter(final Set<String> names) {
        return method -> names.contains(method.getName());
    }

    Method desiredSupplier(final Type resultType) {
        return SUPPLIERS.computeIfAbsent(sourceType, Suppliers::newSuppliersOf)
                        .stream()
                        .filter(method -> resultType.equals(method.getGenericReturnType()))
                        .filter(desired)
                        .findAny()
                        .orElse(null);
    }
}
