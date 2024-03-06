package de.team33.patterns.typing.atlas.sample;

import de.team33.patterns.typing.atlas.Type;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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

@SuppressWarnings("PackageVisibleField")
class Supplying<S> {

    private static final Map<Type<?>, List<Method>> SUPPLIERS = new ConcurrentHashMap<>(0);
    private static final String METHOD_NOT_APPLICABLE = //
            "Method not applicable as supplier!%n" +
            "%n" +
            "    source type: %1$s%n" +
            "    method:      %2$s%n" +
            "%n" +
            "    Maybe the source type is not public? You may also ignore \"%3$s\" to avoid this problem.%n";

    final S source;
    final Type<?> sourceType;
    final Set<String> ignorable;
    final Predicate<Method> desired;

    private final List<Method> suppliers;

    Supplying(final S source, final Collection<String> ignore) {
        this.source = source;
        this.sourceType = Type.of(source.getClass());
        this.suppliers = SUPPLIERS.computeIfAbsent(sourceType, Supplying::suppliers);
        this.ignorable = new HashSet<>(ignore);
        this.desired = nameFilter(ignorable).negate();
    }

    private static Predicate<Method> nameFilter(final Set<String> names) {
        return method -> names.contains(method.getName());
    }

    private static List<Method> suppliers(final Type<?> sourceType) {
        return Methods.publicGetters(sourceType.asClass())
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

    final Supplier<?> desiredSupplier(final Type<?> resultType, final BinaryOperator<Method> preference) {
        return suppliers.stream()
                        .filter(supplier -> {
                            final Type<?> returnType = sourceType.returnTypeOf(supplier);
                            //return resultType.equals(returnType);

                            return Types.isMatching(resultType, sourceType.returnTypeOf(supplier));
                        })
                        .filter(desired)
                        .reduce(preference)
                        .map(this::supplier)
                        .orElse(null);
    }
}
