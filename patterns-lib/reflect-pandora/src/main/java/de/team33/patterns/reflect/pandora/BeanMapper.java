package de.team33.patterns.reflect.pandora;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.Collections.unmodifiableMap;
import static java.util.stream.Collectors.toMap;

public class BeanMapper<S, T> {

    private static final Predicate<Method> IS_OBJECT_METHOD = method -> Object.class.equals(method.getDeclaringClass());
    private static final Predicate<Method> IS_SINGLE_PARAMETER = method -> 1 == method.getParameterCount();
    private static final Predicate<Method> IS_NO_PARAMETER = method -> 0 == method.getParameterCount();

    private final Map<String, Getter> getters;
    private final Map<String, List<Setter>> setters;

    private BeanMapper(final Map<String, Getter> getters, final Map<String, List<Setter>> setters) {
        this.getters = unmodifiableMap(getters);
        this.setters = unmodifiableMap(setters);
    }

    public static <C> BeanMapper<C, C> mapping(final Class<C> subjectClass) {
        return mapping(subjectClass, subjectClass);
    }

    public static <S, T> BeanMapper<S, T> mapping(final Class<S> sourceClass, final Class<T> targetClass) {
        final Map<String, List<Setter>> setters = Stream.of(targetClass.getMethods())
                                                        .filter(IS_OBJECT_METHOD.negate())
                                                        .filter(IS_SINGLE_PARAMETER)
                                                        .map(Setter::new)
                                                        .filter(setter -> setter.prefix.isSetter)
                                                        .collect(TreeMap::new, BeanMapper::putSetter, Map::putAll);
        final Map<String, Getter> getters = Stream.of(sourceClass.getMethods())
                                                  .filter(IS_OBJECT_METHOD.negate())
                                                  .filter(IS_NO_PARAMETER)
                                                  .map(Getter::new)
                                                  .filter(getter -> getter.prefix.isGetter)
                                                  .collect(TreeMap::new, BeanMapper::putGetter, Map::putAll);
        return new BeanMapper<>(getters, setters);
    }

    private static void putGetter(final Map<String, Getter> map, final Getter getter) {
        map.put(getter.name, getter);
    }

    private static void putSetter(final Map<String, List<Setter>> map, final Setter setter) {
        map.computeIfAbsent(setter.name, name -> new LinkedList<>())
           .add(setter);
    }

    private static String normal(final String name) {
        return name.isEmpty() ? name : name.substring(0, 1).toLowerCase() + name.substring(1);
    }

    public final void map(final S source, final T target) {
        getters.forEach((name, getter) -> map(source, name, getter, target));
    }

    private void map(final S source, final String name, final Getter getter, final T target) {
        final Setter setter = Optional.ofNullable(setters.get(name))
                                      .orElseGet(Collections::emptyList)
                                      .stream()
                                      .filter(candidate -> candidate.type.isAssignableFrom(getter.type))
                                      .reduce((left, right) -> left.type.isAssignableFrom(right.type) ? right : left)
                                      .orElseThrow(() -> noSetterFound(name));
        try {
            setter.method.invoke(target, getter.method.invoke(source));
        } catch (final IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    public final Map<String, Object> toMap(final S source) {
        return getters.entrySet()
                      .stream()
                      .collect(TreeMap::new, (map, entry) -> putToMap(map, source, entry), Map::putAll);
    }

    private void putToMap(final Map<String, Object> map, S source, final Map.Entry<String, Getter> entry) {
        try {
            map.put(entry.getKey(), entry.getValue().method.invoke(source));
        } catch (final IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    private static IllegalStateException noSetterFound(String name) {
        return new IllegalStateException("no setter found for property \"" + name + "\"");
    }

    private static class Getter {

        private final Prefix prefix;
        private final String name;
        private final Method method;
        private final Class<?> type;

        Getter(final Method method) {
            assert 0 == method.getParameterCount();
            // - - - - - - - - - - - - - - - - - - - -
            final String name = method.getName();
            this.prefix = Prefix.of(name);
            this.name = normal(name.substring(prefix.length));
            this.method = method;
            this.type = method.getReturnType();
        }
    }

    private static class Setter {

        private final Prefix prefix;
        private final String name;
        private final Method method;
        private final Class<?> type;

        Setter(final Method method) {
            assert 1 == method.getParameterCount();
            // - - - - - - - - - - - - - - - - - - - -
            final String name = method.getName();
            this.prefix = Prefix.of(name);
            this.name = normal(name.substring(prefix.length));
            this.method = method;
            this.type = method.getParameterTypes()[0];
        }
    }

    private enum Prefix {

        is(true, false),
        get(true, false),
        set(false, true),
        none(0, false, false);

        final int length;
        final boolean isGetter;
        final boolean isSetter;

        Prefix(final boolean isGetter, final boolean isSetter) {
            this.length = name().length();
            this.isGetter = isGetter;
            this.isSetter = isSetter;
        }

        Prefix(int length, final boolean isGetter, final boolean isSetter) {
            this.length = length;
            this.isGetter = isGetter;
            this.isSetter = isSetter;
        }

        static Prefix of(final String name) {
            return Stream.of(values())
                         .filter(value -> name.startsWith(value.name()))
                         .findAny()
                         .orElse(none);
        }
    }
}
