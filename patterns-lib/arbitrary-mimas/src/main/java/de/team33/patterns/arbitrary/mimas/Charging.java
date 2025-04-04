package de.team33.patterns.arbitrary.mimas;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.String.format;

final class Charging<S extends Charger, T> extends Supplying<S> {

    private static final String METHOD_NOT_APPLICABLE = Util.load(Charging.class, "setterMethodNotApplicable.txt");
    private static final String NO_SUPPLIER = Util.load(Charging.class, "noSupplierMethodFound.txt");

    private static final Map<Class<?>, List<Method>> SETTERS = new ConcurrentHashMap<>(0);

    private final T target;
    private final Class<?> targetType;

    Charging(final S source, final T target, final Collection<String> ignore) {
        super(source, ignore);
        this.target = target;
        this.targetType = target.getClass();
    }

    private static List<Method> newSettersOf(final Class<?> targetType) {
        return Methods.publicSetters(targetType)
                      .collect(Collectors.toList());
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
                throw new LocalException(format(METHOD_NOT_APPLICABLE,
                                                targetType,
                                                setter.toGenericString(),
                                                setter.getName()), e);
            }
        };
    }

    final T result() {
        desiredSetters().forEach(this::apply);
        return target;
    }

    private void apply(final Method setter) {
        final Type valueType = setter.getGenericParameterTypes()[0];
        final Supplier<?> supplier = desiredSupplier(valueType, preference(setter));
        if (null == supplier) {
            throw new LocalException(this, setter, valueType);
        } else {
            setter(setter).accept(supplier.get());
        }
    }

    private static BinaryOperator<Method> preference(final Method setter) {
        return (left, right) -> preference(Methods.normalName(setter), left, right);
    }

    private static Method preference(final String setterName, final Method left, final Method right) {
        return Methods.normalName(right).equals(setterName) ? right : left;
    }

    private static final class LocalException extends UnfitConditionException {

        LocalException(final String message, final Throwable cause) {
            super(message, cause);
        }

        LocalException(final Charging<?, ?> charging, final Method setter, final Type valueType) {
            super(missingMessage(charging, setter, valueType), null);
        }

        private static String missingMessage(final Charging<?, ?> charging, final Method setter, final Type valueType) {
            final Types.Naming naming = Types.naming(valueType);
            final String name1 = naming.parameterizedName(valueType);
            final String name2 = Methods.normalName(setter);
            return format(NO_SUPPLIER, charging.sourceType, charging.targetType,
                          setter.toGenericString(), setter.getName(), name1, name2);
        }
    }
}
