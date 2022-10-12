package de.team33.patterns.random.tarvos;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static de.team33.patterns.random.tarvos.Chargers.INIT_FAILED;
import static java.lang.String.format;

/**
 * A utility for the blanket initialization of instances composed of properties that can be set using typical setters.
 * <p>
 * The values for this are supplied by a source instance of a specific type.
 */
public interface Charger {

    /**
     * Initializes the properties of a given {@code target} instance with values from a given {@code source} instance
     * and returns the initialized {@code target}.
     * <p>
     * In order for a property to be initialized, the {@code target}'s class must provide a corresponding setter
     * that is actually accessible from this {@link Charger}. In addition, the class of the {@code source} must
     * provide a parameterless method that can return an appropriate value for the property in question.
     */
    default <T> T charge(final T target, final String... ignore) {
        final Set<String> ignorable = new HashSet<>(Arrays.asList(ignore));
        Chargers.settersOf(target.getClass())
                .filter(setter -> !ignorable.contains(setter.getName()))
                .forEach(setter -> {
                    final Type type = setter.getGenericParameterTypes()[0];
                    final Method supplier = Chargers.supplierOf(getClass(), type);
                    if (null == supplier) {
                        Chargers.logMissing(getClass(), type);
                    } else {
                        Chargers.invoke(target, this, setter, supplier);
                    }
                });
        return target;
    }
}
