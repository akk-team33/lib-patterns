package de.team33.patterns.reflect.pandora;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Stream;

import static de.team33.patterns.reflect.pandora.Util.normal;
import static java.util.Collections.unmodifiableMap;

public class BeanMapper<S, T> {

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
                                                        .filter(Util::isNoObjectMethod)
                                                        .filter(Util::isSetter)
                                                        .map(Setter::new)
                                                        .filter(setter -> setter.prefix.isSetter)
                                                        .collect(TreeMap::new, BeanMapper::putSetter, Map::putAll);
        final Map<String, Getter> getters = Stream.of(sourceClass.getMethods())
                                                  .filter(Util::isNoObjectMethod)
                                                  .filter(Util::isGetter)
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
            this.name = Util.normal(name.substring(prefix.length));
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

}
