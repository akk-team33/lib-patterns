package de.team33.patterns.properties.e1;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Locale;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

/**
 * A utility for handling {@link Method}s. In particular, it can generate a {@link Mapping} based on the
 * getter {@link Method}s of a specific class.
 *
 * @see #mapping(Class)
 */
public final class Methods {

    private static final int NOT_SIGNIFICANT = Modifier.STATIC | Modifier.NATIVE;
    private static final String PF_GETTER = "get";
    private static final String PF_BOOL_GETTER = "is";

    private Methods() {
    }

    private static boolean isGetter(final Method method) {
        return isSignificant(method) && isParameterCount(method, 0) && isPrefixed(method, PF_GETTER, PF_BOOL_GETTER);
    }

    private static boolean isPrefixed(final Method method, final String ... prefixes) {
        return Stream.of(prefixes).anyMatch(prefix -> method.getName().startsWith(prefix));
    }

    private static boolean isParameterCount(final Method method, final int count) {
        return method.getParameterCount() == count;
    }

    private static boolean isSignificant(final Method method) {
        return isSignificant(method.getModifiers());
    }

    private static boolean isSignificant(final int modifiers) {
        return 0 == (modifiers & NOT_SIGNIFICANT);
    }

    private static String getterName(final Method method) {
        final String name = method.getName();
        final String prefix = Stream.of(PF_GETTER, PF_BOOL_GETTER)
                                    .filter(name::startsWith)
                                    .findAny()
                                    .orElseThrow(() -> new IllegalStateException("method is not a getter: " + method));
        final int index0 = prefix.length();
        final int index1 = (index0 < name.length()) ? (index0 + 1) : index0;
        return name.substring(index0, index1).toLowerCase(Locale.ROOT) + name.substring(index1);
    }

    private static <T> Function<T, Object> newGetter(final Method method) {
        return origin -> {
            try {
                return method.invoke(origin);
            } catch (final IllegalAccessException | InvocationTargetException e) {
                throw new IllegalArgumentException(e.getMessage(), e);
            }
        };
    }

    /**
     * Returns a {@link Mapping} made up of the public getters of a given class.
     *
     * @param <T> The type whose properties are to be mapped.
     */
    public static <T> Mapping<T> mapping(final Class<T> tClass) {
        final Map<String, Function<T, Object>> getters = Stream.of(tClass.getMethods())
                                                               .filter(Methods::isGetter)
                                                               .collect(toMap(Methods::getterName,
                                                                              Methods::newGetter));
        return origin -> MappingUtil.mappingOperation(getters, origin);
    }

    public static <T> BiMapping<T> biMapping(final Class<T> tClass) {
        final Supplier<Selector<T>> newSelector = Selector::new;
        final Map<String, Accessor<T, Object>> methods = Stream.of(tClass.getMethods())
                                                               .collect(newSelector, Selector::add, Selector::addAll)
                                                               .toMethods();
        return new AccessorMapping<>(methods);
    }

    private static class Selector<T> {

        private void add(final Method method) {
            throw new UnsupportedOperationException("not yet implemented");
        }

        private void addAll(final Selector<T> other) {
            throw new UnsupportedOperationException("Unexpectedly called");
        }

        public <T> Map<String, Accessor<T, Object>> toMethods() {
            throw new UnsupportedOperationException("not yet implemented");
        }
    }

    private enum Prefix {
        get, is, set
    }

    private enum Typ {

        GETTER(Prefix.get, Prefix.is),
        SETTER(Prefix.set),
        OTHER();

        private final EnumSet<Prefix> prefixes;

        Typ(final Prefix ... prefixes) {
            this.prefixes = (0 == prefixes.length)
                    ? EnumSet.noneOf(Prefix.class)
                    : EnumSet.copyOf(Arrays.asList(prefixes));
        }
    }

}
