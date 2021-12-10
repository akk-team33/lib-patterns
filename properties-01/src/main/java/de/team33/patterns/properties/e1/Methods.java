package de.team33.patterns.properties.e1;

import de.team33.patterns.exceptional.e1.Converter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.lang.String.format;
import static java.util.Collections.unmodifiableSet;
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

    private static boolean isPrefixed(final Method method, final String... prefixes) {
        return Stream.of(prefixes).anyMatch(prefix -> method.getName().startsWith(prefix));
    }

    private static boolean isSingleParam(final Method method) {
        return isParameterCount(method, 1);
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

    /**
     * Returns a {@link BiMapping} made up of the public getters and setters of a given class.
     *
     * @param <T> The type whose properties are to be mapped.
     */
    public static <T> BiMapping<T> biMapping(final Class<T> tClass) {
        final Supplier<BiSelector<T>> newSelector = BiSelector::new;
        final Map<String, Accessor<T, Object>> methods = Stream.of(tClass.getMethods())
                                                               .filter(Methods::isSignificant)
                                                               .map(Entry::new)
                                                               .collect(newSelector,
                                                                        BiSelector::add,
                                                                        BiSelector::addAll)
                                                               .toMethods();
        return new AccMapping<>(methods);
    }

    private static class Selector<T> {
    }

    private static class BiSelector<T> {

        private static final Converter CONVERTER =
                Converter.using(cause -> new IllegalArgumentException(cause.getMessage(), cause));

        private final List<Entry> getters = new LinkedList<>();
        private final Map<String, List<Entry>> setters = new TreeMap<>();

        private static Method setter(final Class<?> paramType, final List<Entry> setters) {
            return setters.stream()
                          .map(setter -> setter.method)
                          .filter(setter -> setter.getParameterTypes()[0].isAssignableFrom(paramType))
                          .findAny()
                          .orElseThrow(() -> new IllegalStateException(
                                  format("No setter found matching parameter type %s in %s", paramType, setters)));
        }

        private void add(final Entry entry) {
            if (entry.prefix.isGetter() && isParameterCount(entry.method, 0)) {
                getters.add(entry);
            }
            if (entry.prefix.isSetter() && isParameterCount(entry.method, 1)) {
                setters.computeIfAbsent(entry.normalName(), name -> new LinkedList<>())
                       .add(entry);
            }
        }

        private void addAll(final BiSelector<T> other) {
            throw new UnsupportedOperationException("Unexpectedly called");
        }

        private Map<String, Accessor<T, Object>> toMethods() {
            return getters.stream()
                          .collect(toMap(Entry::normalName, this::toAccessor));
        }

        private Accessor<T, Object> toAccessor(final Entry getterEntry) {
            final Method getter = getterEntry.method;
            final String name = getterEntry.normalName();
            final Method setter = setter(getter.getReturnType(), setters.get(name));
            return toAccessor(getter, setter);
        }

        private Accessor<T, Object> toAccessor(final Method getter, final Method setter) {
            return Accessor.combine(CONVERTER.function(getter::invoke), CONVERTER.biConsumer(setter::invoke));
        }
    }

    private static class Entry {

        private final Prefix prefix;
        private final Method method;

        Entry(final Method method) {
            this.prefix = Stream.of(Prefix.values()).filter(isPrefix(method)).findAny().orElse(Prefix.NONE);
            this.method = method;
        }

        static Predicate<Prefix> isPrefix(final Method method) {
            return prefix -> method.getName().startsWith(prefix.name());
        }

        final String normalName() {
            final String methodName = method.getName();
            final int index0 = prefix.length;
            final int index1 = index0 + ((index0 < methodName.length()) ? 1 : 0);
            return methodName.substring(index0, index1).toLowerCase(Locale.ROOT) + methodName.substring(index1);
        }
    }

    private enum Prefix {

        get(true), is(true), set(true), NONE(false);

        private static final Set<Prefix> GETTERS = unmodifiableSet(EnumSet.of(get, is));
        private static final Set<Prefix> SETTERS = unmodifiableSet(EnumSet.of(set));

        private final int length;

        Prefix(final boolean real) {
            this.length = real ? name().length() : 0;
        }

        final boolean isGetter() {
            return GETTERS.contains(this);
        }

        final boolean isSetter() {
            return SETTERS.contains(this);
        }
    }
}
