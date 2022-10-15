package de.team33.patterns.random.tarvos;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("PackageVisibleField")
class Supplying<S> {

    private static final Map<Class<?>, List<Method>> SUPPLIERS = new ConcurrentHashMap<>(0);
    private static final Predicate<Object> ANY_METHOD = method -> true;
    private static final String METHOD_NOT_APPLICABLE = Util.load(Supplying.class, "supplierMethodNotApplicable.txt");

    final S source;
    final Class<?> sourceType;

    private final List<Method> suppliers;

    Supplying(final S source) {
        this.source = source;
        this.sourceType = source.getClass();
        this.suppliers = SUPPLIERS.computeIfAbsent(sourceType, Supplying::suppliersOf);
    }

    private static List<Method> suppliersOf(final Class<?> sourceType) {
        return Stream.of(sourceType.getMethods())
                     .filter(method -> !Object.class.equals(method.getDeclaringClass()))
                     .filter(Methods::isSupplier)
                     .collect(Collectors.toList());
    }

    final Supplier<?> desiredSupplier(final Type resultType) {
        return desiredSupplier(resultType, ANY_METHOD);
    }

    final Supplier<?> desiredSupplier(final Type resultType, final Predicate<? super Method> desired) {
        return suppliers.stream()
                        .filter(method -> resultType.equals(method.getGenericReturnType()))
                        .filter(desired)
                        .findAny()
                        .map(this::supplier)
                        .orElse(null);
    }

    private Supplier<?> supplier(final Method method) {
        return () -> {
            try {
                return method.invoke(source);
            } catch (final IllegalAccessException | InvocationTargetException | IllegalArgumentException e) {
                throw new IllegalStateException(String.format(METHOD_NOT_APPLICABLE,
                                                              sourceType,
                                                              method.toGenericString()), e);
            }
        };
    }
}
