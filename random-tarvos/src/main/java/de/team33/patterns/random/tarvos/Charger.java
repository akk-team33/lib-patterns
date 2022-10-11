package de.team33.patterns.random.tarvos;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.String.format;

/**
 * A utility for the blanket initialization of instances composed of properties that can be set using typical setters.
 * <p>
 * The values for this are supplied by a source instance of a specific type.
 *
 * @param <S> The source type
 */
public class Charger<S> {

    private static final Logger LOG = Logger.getLogger(Charger.class.getCanonicalName());
    private static final String NO_SUPPLIER = "No appropriate supplier method found for %s%n%n" +
            "    Consider defining a method in the source type <%s> that looks something like this:%n%n" +
            "    public final %s next%s() {%n" +
            "        return ...;%n" +
            "    }%n";
    public static final String INIT_FAILED = "Initialization failed:%n%n" +
            "    setter:   %s%n" +
            "    supplier: %s%n";

    private final Class<S> sourceType;
    private final Map<Class<?>, List<Method>> setters = new ConcurrentHashMap<>(0);
    private final Map<Type, Method> suppliers = new ConcurrentHashMap<>(0);

    public Charger(final Class<S> sourceType) {
        this.sourceType = sourceType;
    }

    private static List<Method> newSettersOf(final Class<?> targetClass) {
        return Stream.of(targetClass.getMethods())
                     .filter(Methods::isSetter)
                     .collect(Collectors.toList());
    }

    /**
     * Initializes the properties of a given {@code target} instance with values from a given {@code source} instance
     * and returns the initialized {@code target}.
     * <p>
     * In order for a property to be initialized, the {@code target}'s class must provide a corresponding setter
     * that is actually accessible from this {@link Charger}. In addition, the class of the {@code source} must
     * provide a parameterless method that can return an appropriate value for the property in question.
     */
    public final <T> T charge(final T target, final S source) {
        final Class<?> targetClass = target.getClass();
        for (final Method setter : settersOf(targetClass)) {
            final Type type = setter.getGenericParameterTypes()[0];
            final Method supplier = supplierOf(type);
            if (null == supplier) {
                logMissing(type);
            } else {
                invoke(target, source, setter, supplier);
            }
        }
        return target;
    }

    @SuppressWarnings("OverlyBroadCatchBlock")
    private <T> void invoke(final T target, final S source, final Method setter, final Method supplier) {
        try {
            final Object value = supplier.invoke(source);
            setter.invoke(target, value);
        } catch (final IllegalAccessException | InvocationTargetException | RuntimeException e) {
            LOG.log(Level.WARNING, e, () -> format(INIT_FAILED,
                                                   setter.toGenericString(),
                                                   supplier.toGenericString()));
        }
    }

    private void logMissing(final Type type) {
        final Naming naming = Naming.of(type);
        LOG.warning(() -> {
            final String name1 = naming.parameterizedName(type);
            final String name2 = naming.simpleName(type);
            return format(NO_SUPPLIER, type, sourceType.getSimpleName(), name1, name2);
        });
    }

    private Method supplierOf(final Type type) {
        return suppliers.computeIfAbsent(type, this::findSupplierOf);
    }

    private Method findSupplierOf(final Type type) {
        return Stream.of(sourceType.getMethods())
                     .filter(method -> !Object.class.equals(method.getDeclaringClass()))
                     .filter(Methods::isSupplier)
                     .filter(method -> type.equals(method.getGenericReturnType()))
                     .findAny()
                     .orElse(null);
    }

    private Iterable<Method> settersOf(final Class<?> targetClass) {
        return setters.computeIfAbsent(targetClass, Charger::newSettersOf);
    }

    private enum Naming {

        CLASS(Class.class, Class::getSimpleName, type -> ""),
        PARAMETERIZED(ParameterizedType.class, Naming::toSimpleName, Naming::toParameters),
        OTHER(Type.class, Type::getTypeName, type -> "");

        private static String toParameters(final ParameterizedType type) {
            return Arrays.stream(type.getActualTypeArguments())
                         .map(pType -> of(pType).simpleName(pType))
                         .collect(Collectors.joining(", ", "<", ">"));
        }

        private static String toSimpleName(final ParameterizedType type) {
            final Type rawType = type.getRawType();
            return of(rawType).simpleName(rawType);
        }

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

    private static final class Methods {

        private static final int SYNTHETIC = 0x00001000;
        private static final int NON_INSTANCE = Modifier.STATIC | Modifier.NATIVE | SYNTHETIC;

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
