package de.team33.patterns.properties.e1;

import de.team33.patterns.exceptional.dione.Converter;

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
import java.util.stream.Collector;
import java.util.stream.Stream;

import static java.lang.String.format;
import static java.util.Collections.unmodifiableSet;
import static java.util.stream.Collectors.toMap;

/**
 * A utility for handling {@link Method}s. In particular, it can generate a {@link Mapping} based on the
 * getter {@link Method}s of a specific class.
 *
 * @see #mapping(Class)
 * @deprecated Further development is discontinued due to relatively high complexity with little benefit and this
 * package/module may be removed in a future release.
 */
@Deprecated
public final class Methods {

    private Methods() {
    }

    /**
     * Returns a {@link Mapping} made up of the public getters of a given class.
     *
     * @param <T> The type whose properties are to be mapped.
     */
    public static <T> Mapping<T> mapping(final Class<T> tClass) {
        final Collector<Entry, ?, Map<String, Function<T, Object>>> collector =
                toMap(Entry::normalName,
                      entry -> Mutual.CONVERTER.function(entry.method::invoke));
        final Map<String, Function<T, Object>> getters = Stream.of(tClass.getMethods())
                                                               .filter(Mutual::isSignificant)
                                                               .filter(Mutual::isNoParameter)
                                                               .map(Entry::new)
                                                               .filter(entry -> entry.prefix.isGetter())
                                                               .collect(collector);
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
                                                               .filter(Mutual::isSignificant)
                                                               .map(Entry::new)
                                                               .collect(newSelector,
                                                                        BiSelector::add,
                                                                        BiSelector::addAll)
                                                               .toMethods();
        return new AccMapping<>(methods);
    }

    private enum Prefix {

        get(true), is(true), set(true), NONE(false);

        private static final Set<Prefix> GETTERS = unmodifiableSet(EnumSet.of(get, is));
        private static final Set<Prefix> SETTERS = unmodifiableSet(EnumSet.of(set));

        final int length;

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

    private static final class Mutual {

        static final int SYNTHETIC = 0x00001000;
        static final int NOT_SIGNIFICANT = Modifier.STATIC | Modifier.NATIVE | SYNTHETIC;
        static final Converter CONVERTER =
                Converter.using(cause -> new IllegalArgumentException(cause.getMessage(), cause));

        static boolean isNoParameter(final Method method) {
            return isParameterCount(method, 0);
        }

        static boolean isParameterCount(final Method method, final int count) {
            return method.getParameterCount() == count;
        }

        static boolean isSignificant(final Method method) {
            return isSignificant(method.getModifiers());
        }

        static boolean isSignificant(final int modifiers) {
            return 0 == (modifiers & NOT_SIGNIFICANT);
        }
    }

    private static class BiSelector<T> {

        private final List<Entry> getters = new LinkedList<>();
        private final Map<String, List<Entry>> setters = new TreeMap<>();

        private static Method setter(final Class<?> paramType, final List<? extends Entry> setters) {
            return setters.stream()
                          .map(setter -> setter.method)
                          .filter(setter -> setter.getParameterTypes()[0].isAssignableFrom(paramType))
                          .findAny()
                          .orElseThrow(() -> new IllegalStateException(
                                  format("No setter found matching parameter type %s in %s", paramType, setters)));
        }

        final void add(final Entry entry) {
            if (entry.prefix.isGetter() && Mutual.isParameterCount(entry.method, 0)) {
                getters.add(entry);
            }
            if (entry.prefix.isSetter() && Mutual.isParameterCount(entry.method, 1)) {
                setters.computeIfAbsent(entry.normalName(), name -> new LinkedList<>())
                       .add(entry);
            }
        }

        final void addAll(final BiSelector<T> other) {
            throw new UnsupportedOperationException("Unexpectedly called");
        }

        final Map<String, Accessor<T, Object>> toMethods() {
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
            return Accessor.combine(Mutual.CONVERTER.function(getter::invoke),
                                    Mutual.CONVERTER.biConsumer(setter::invoke));
        }
    }

    private static class Entry {

        final Prefix prefix;
        final Method method;

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

        @Override
        public final String toString() {
            return method.toString();
        }
    }
}
