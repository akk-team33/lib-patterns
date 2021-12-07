package de.team33.patterns.properties.e1;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
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
}
