package de.team33.patterns.random.tarvos;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

import static de.team33.patterns.random.tarvos.Types.naming;
import static java.lang.String.format;

final class Initiating<S extends Initiator, T> extends Suppliers {

    private static final Logger LOG = Logger.getLogger(Initiating.class.getCanonicalName());
    private static final String NO_SUPPLIER = "No appropriate supplier method found ...%n%n" +
            "    target type:     %s%n" +
            "    parameter index: %s%n" +
            "    parameter type:  %s%n" +
            "    source type: %s%n%n" +
            "    Consider defining a method in the source type that looks something like this:%n%n" +
            "    public final %s next%s() {%n" +
            "        return ...;%n" +
            "    }%n";

    private final Class<T> targetType;
    private final Constructor<T> constructor;
    private final Type[] parameterTypes;

    Initiating(final S source, final Class<T> targetType) {
        super(source, Collections.emptyList());
        this.targetType = targetType;
        this.constructor = constructor(targetType);
        this.parameterTypes = constructor.getGenericParameterTypes();
    }

    private static <T> Constructor<T> constructor(final Class<T> targetType) {
        try {
            final Class<?>[] types = Stream.of(targetType.getConstructors())
                                           .reduce(Initiating::largest)
                                           .map(Constructor::getParameterTypes)
                                           .orElseThrow(() -> new NoSuchMethodException(
                                                   "no public constructor: " + targetType));
            return targetType.getConstructor(types);
        } catch (final NoSuchMethodException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    private static Constructor<?> largest(final Constructor<?> left, final Constructor<?> right) {
        return (left.getParameterCount() < right.getParameterCount()) ? right : left;
    }

    static void defaultLog(final Supplier<String> message, final Exception cause) {
        LOG.log(Level.WARNING, cause, message);
    }

    private Supplier<String> missingMessage(final int parameterIndex, final Type parameterType) {
        final Types.Naming naming = naming(parameterType);
        return () -> {
            final String name1 = naming.parameterizedName(parameterType);
            final String name2 = naming.simpleName(parameterType);
            return format(NO_SUPPLIER, targetType, parameterIndex, parameterType, sourceType,
                          name1, name2);
        };
    }

    private Object[] arguments() {
        final int length = parameterTypes.length;
        final Object[] result = new Object[length];
        for (int i = 0; i < length; i++) {
            final Type parameterType = parameterTypes[i];
            final Method supplier = desiredSupplier(parameterType);
            if (null == supplier) {
                defaultLog(missingMessage(i, parameterType), null);
                result[i] = Types.defaultValue(parameterType);
            } else {
                try {
                    result[i] = supplier.invoke(source);
                } catch (final IllegalAccessException | InvocationTargetException | RuntimeException e) {
                    throw new IllegalStateException(e.getMessage(), e);
                }
            }
        }
        return result;
    }

    final T result() {
        try {
            return constructor.newInstance(arguments());
        } catch (final InstantiationException | IllegalAccessException |
                InvocationTargetException | IllegalArgumentException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }
}
