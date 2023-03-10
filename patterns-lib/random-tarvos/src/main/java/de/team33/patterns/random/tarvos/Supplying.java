package de.team33.patterns.random.tarvos;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BinaryOperator;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("PackageVisibleField")
class Supplying<S> {

    private static final Map<Class<?>, List<Method>> SUPPLIERS = new ConcurrentHashMap<>(0);
    private static final String METHOD_NOT_APPLICABLE = Util.load(Supplying.class, "supplierMethodNotApplicable.txt");

    final S source;
    final Class<?> sourceType;
    final Set<String> ignorable;
    final Predicate<Method> desired;

    private final List<Method> suppliers;

    Supplying(final S source, final Collection<String> ignore) {
        this.source = source;
        this.sourceType = source.getClass();
        this.suppliers = SUPPLIERS.computeIfAbsent(sourceType, Supplying::suppliers);
        this.ignorable = new HashSet<>(ignore);
        this.desired = nameFilter(ignorable).negate();
    }

    private static Predicate<Method> nameFilter(final Set<String> names) {
        return method -> names.contains(method.getName());
    }

    private static List<Method> suppliers(final Class<?> sourceType) {
        return Methods.publicGetters(sourceType)
                      .collect(Collectors.toList());
    }

    private Supplier<?> supplier(final Method method) {
        return () -> {
            try {
                return method.invoke(source);
            } catch (final IllegalAccessException | InvocationTargetException | IllegalArgumentException e) {
                throw new UnfitConditionException(String.format(METHOD_NOT_APPLICABLE,
                                                                sourceType,
                                                                method.toGenericString(),
                                                                method.getName()), e);
            }
        };
    }

    final Supplier<?> desiredSupplier(final Type resultType, final BinaryOperator<Method> preference) {
        return suppliers.stream()
                        .filter(supplier -> Types.isMatching(resultType, supplier.getGenericReturnType()))
                        .filter(desired)
                        .reduce(preference)
                        .map(this::supplier)
                        .orElse(null);
    }
}
