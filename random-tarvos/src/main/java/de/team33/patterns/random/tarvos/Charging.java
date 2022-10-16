package de.team33.patterns.random.tarvos;

import de.team33.patterns.random.tarvos.Types.Naming;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static de.team33.patterns.random.tarvos.Types.naming;
import static java.lang.String.format;

final class Charging<S extends Charger, T> extends Supplying<S> {

    private static final String METHOD_NOT_APPLICABLE = Util.load(Charging.class, "setterMethodNotApplicable.txt");
    private static final String NO_SUPPLIER = Util.load(Charging.class, "noSupplierMethodFound.txt");

    private static final Map<Class<?>, List<Method>> SETTERS = new ConcurrentHashMap<>(0);

    private final T target;
    private final Class<?> targetType;
    private final Predicate<Method> desired;

    Charging(final S source, final T target, final Collection<String> ignore) {
        super(source);
        this.target = target;
        this.targetType = target.getClass();
        this.desired = nameFilter(new HashSet<>(ignore)).negate();
    }

    private static List<Method> newSettersOf(final Class<?> targetType) {
        return Stream.of(targetType.getMethods())
                     .filter(Methods::isSetter)
                     .collect(Collectors.toList());
    }

    private static Predicate<Method> nameFilter(final Set<String> names) {
        return method -> names.contains(method.getName());
    }

    private Stream<Method> desiredSetters() {
        return SETTERS.computeIfAbsent(targetType, Charging::newSettersOf)
                      .stream()
                      .filter(desired);
    }

    private Consumer<Object> setter(final Method setter) {
        return value -> {
            try {
                setter.invoke(target, value);
            } catch (final IllegalAccessException | InvocationTargetException | IllegalArgumentException e) {
                throw new ChargingException(format(METHOD_NOT_APPLICABLE,
                                                   targetType,
                                                   setter.toGenericString(),
                                                   setter.getName()), e);
            }
        };
    }

    final T result() {
        desiredSetters().forEach(setter -> {
            final Type valueType = setter.getGenericParameterTypes()[0];
            final Supplier<?> supplier = desiredSupplier(valueType, desired);
            if (null == supplier) {
                throw new ChargingException(this, setter, valueType);
            } else {
                setter(setter).accept(supplier.get());
            }
        });
        return target;
    }

    private static final class ChargingException extends RuntimeException {

        ChargingException(final String message, final Throwable cause) {
            super(message, cause);
        }

        ChargingException(final Charging<?, ?> charging, final Method setter, final Type valueType) {
            this(missingMessage(charging, setter, valueType), null);
        }

        private static String missingMessage(final Charging<?, ?> charging, final Method setter, final Type valueType) {
            final Naming naming = naming(valueType);
            final String name1 = naming.parameterizedName(valueType);
            final String name2 = naming.simpleName(valueType);
            return format(NO_SUPPLIER, charging.sourceType, charging.targetType,
                          setter.toGenericString(), setter.getName(), name1, name2);
        }
    }
}
