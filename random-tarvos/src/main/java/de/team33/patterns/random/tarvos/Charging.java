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
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static de.team33.patterns.random.tarvos.Types.naming;
import static java.lang.String.format;

final class Charging<S extends Charger, T> extends Supplying {

    private static final Logger LOG = Logger.getLogger(Charging.class.getCanonicalName());
    private static final String NO_SUPPLIER = "No appropriate supplier method found ...%n%n" +
            "    target type: %s%n" +
            "    setter:      %s%n" +
            "    type:        %s%n" +
            "    source type: %s%n%n" +
            "    Consider ignoring \"%s\" or defining a method in the source type that looks something like this:%n%n" +
            "    public final %s next%s() {%n" +
            "        return ...;%n" +
            "    }%n";
    private static final String INIT_FAILED = "Initialization failed:%n%n" +
            "    target type: %s%n" +
            "    setter:      %s%n" +
            "    source type: %s%n" +
            "    supplier: %s%n%n" +
            "    Consider ignoring the causing method.";

    private static final Map<Class<?>, List<Method>> SETTERS = new ConcurrentHashMap<>(0);

    private final S source;
    private final Class<?> sourceType;
    private final T target;
    private final Class<?> targetType;
    private final Predicate<Method> desired;

    Charging(final S source, final T target, final Collection<String> ignore) {
        super(source.getClass());
        this.source = source;
        this.sourceType = source.getClass();
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

    static void defaultLog(final Supplier<String> message, final Exception cause) {
        LOG.log(Level.WARNING, cause, message);
    }

    @SuppressWarnings("OverlyBroadCatchBlock")
    private void invoke(final Method setter, final Method supplier) {
        try {
            final Object value = supplier.invoke(source);
            setter.invoke(target, value);
        } catch (final IllegalAccessException | InvocationTargetException | RuntimeException e) {
            source.chargerLog(() -> format(INIT_FAILED,
                                           targetType,
                                           setter.toGenericString(),
                                           sourceType,
                                           supplier.toGenericString()), e);
        }
    }

    private Supplier<String> missingMessage(final Method setter, final Type resultType) {
        final Naming naming = naming(resultType);
        return () -> {
            final String name1 = naming.parameterizedName(resultType);
            final String name2 = naming.simpleName(resultType);
            return format(NO_SUPPLIER, setter.getDeclaringClass(), setter.toGenericString(), resultType, sourceType,
                          setter.getName(), name1, name2);
        };
    }

    private Stream<Method> desiredSetters() {
        return SETTERS.computeIfAbsent(targetType, Charging::newSettersOf)
                      .stream()
                      .filter(desired);
    }

    final T result() {
        desiredSetters().forEach(setter -> {
            final Type valueType = setter.getGenericParameterTypes()[0];
            final Method supplier = desiredSupplier(valueType, desired);
            if (null == supplier) {
                source.chargerLog(missingMessage(setter, valueType), null);
            } else {
                invoke(setter, supplier);
            }
        });
        return target;
    }
}
