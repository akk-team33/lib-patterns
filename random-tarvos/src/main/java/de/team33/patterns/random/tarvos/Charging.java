package de.team33.patterns.random.tarvos;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.String.format;

final class Charging<T> {

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
            "    setter:   %s%n" +
            "    supplier: %s%n";

    private static final Map<Class<?>, List<Method>> SETTERS = new ConcurrentHashMap<>(0);

    private final Charger source;
    private final Class<?> sourceType;
    private final T target;
    private final Class<?> targetType;
    private final Predicate<Method> desired;

    Charging(final Charger source, final T target, final Collection<String> ignore) {
        this.source = source;
        this.sourceType = source.getClass();
        this.target = target;
        this.targetType = target.getClass();
        this.desired = nameFilter(new HashSet<>(ignore)).negate();
    }

    private static List<Method> newSettersOf(final Class<?> targetClass) {
        return Stream.of(targetClass.getMethods())
                     .filter(Methods::isSetter)
                     .collect(Collectors.toList());
    }

    @SuppressWarnings("OverlyBroadCatchBlock")
    static <T, S> void invoke(final T target, final S source, final Method setter, final Method supplier) {
        try {
            final Object value = supplier.invoke(source);
            setter.invoke(target, value);
        } catch (final IllegalAccessException | InvocationTargetException | RuntimeException e) {
            LOG.log(Level.WARNING, e, () -> format(INIT_FAILED,
                                                   setter.toGenericString(),
                                                   supplier.toGenericString()));
        }
    }

    static Supplier<String> missingMessage(final Class<?> sourceType, final Method setter, final Type resultType) {
        final Naming naming = Naming.of(resultType);
        return () -> {
            final String name1 = naming.parameterizedName(resultType);
            final String name2 = naming.simpleName(resultType);
            return format(NO_SUPPLIER, setter.getDeclaringClass(), setter.toGenericString(), resultType, sourceType,
                          setter.getName(), name1, name2);
        };
    }

    static Method supplierOf(final Class<?> sourceType, final Type resultType, Predicate<Method> ignoring) {
        return Stream.of(sourceType.getMethods())
                     .filter(method -> !Object.class.equals(method.getDeclaringClass()))
                     .filter(Methods::isSupplier)
                     .filter(method -> resultType.equals(method.getGenericReturnType()))
                     .filter(ignoring)
                     .findAny()
                     .orElse(null);
    }

    static Stream<Method> settersOf(final Class<?> targetClass) {
        return SETTERS.computeIfAbsent(targetClass, Charging::newSettersOf)
                      .stream();
    }

    static void log(final Supplier<String> message, final Exception cause) {
        LOG.log(Level.WARNING, cause, message);
    }

    private static Predicate<Method> nameFilter(final Set<String> names) {
        return method -> names.contains(method.getName());
    }

    private static Predicate<Method> ignoring(final Set<String> ignorable) {
        return method -> !ignorable.contains(method.getName());
    }

    final T result() {
        settersOf(targetType)
                .filter(desired)
                .forEach(setter -> {
                    final Type type = setter.getGenericParameterTypes()[0];
                    final Method supplier = supplierOf(sourceType, type, desired);
                    if (null == supplier) {
                        source.chargerLog(missingMessage(sourceType, setter, type), null);
                    } else {
                        invoke(target, source, setter, supplier);
                    }
                });
        return target;
    }

    private enum Naming {

        CLASS(Class.class, Class::getSimpleName, type -> ""),
        PARAMETERIZED(ParameterizedType.class, Naming::toSimpleName, Naming::toParameters),
        OTHER(Type.class, Type::getTypeName, type -> "");

        private final Class<?> typeClass;
        @SuppressWarnings("rawtypes")
        private final Function toSimpleName;
        @SuppressWarnings("rawtypes")
        private final Function toParameters;

        <T extends Type> Naming(final Class<T> typeClass,
                                final Function<T, String> toSimpleName,
                                final Function<T, String> toParameters) {
            this.typeClass = typeClass;
            this.toSimpleName = toSimpleName;
            this.toParameters = toParameters;
        }

        private static String toParameters(final ParameterizedType type) {
            return Arrays.stream(type.getActualTypeArguments())
                         .map(pType -> of(pType).simpleName(pType))
                         .collect(Collectors.joining(", ", "<", ">"));
        }

        private static String toSimpleName(final ParameterizedType type) {
            final Type rawType = type.getRawType();
            return of(rawType).simpleName(rawType);
        }

        static Naming of(final Type type) {
            final Class<? extends Type> typeClass = type.getClass();
            return Stream.of(values())
                         .filter(value -> value.typeClass.isAssignableFrom(typeClass))
                         .findAny()
                         .orElseThrow(() -> new NoSuchElementException(format("No entry found for type <%s>",
                                                                              typeClass)));
        }

        final String simpleName(final Type type) {
            //noinspection unchecked
            return (String) toSimpleName.apply(type);
        }

        final String parameterizedName(final Type type) {
            //noinspection unchecked
            return simpleName(type) + toParameters.apply(type);
        }
    }

    static final class Methods {

        private static final int SYNTHETIC = 0x00001000;
        private static final int NON_INSTANCE = Modifier.STATIC | Modifier.NATIVE | SYNTHETIC;

        private Methods() {
        }

        private static boolean isInstance(final Method method) {
            return isInstance(method.getModifiers());
        }

        private static boolean isInstance(final int modifiers) {
            return 0 == (modifiers & NON_INSTANCE);
        }

        static boolean isSetter(final Method method) {
            return isInstance(method) && method.getName().startsWith("set") && (1 == method.getParameterCount());
        }

        static boolean isSupplier(final Method method) {
            return isInstance(method) && (0 == method.getParameterCount());
        }
    }
}
