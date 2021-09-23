package de.team33.patterns.properties.e1a;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Stream;

class Methods {

    private static final int NOT_SIGNIFICANT = Modifier.STATIC | Modifier.NATIVE;

    static boolean isSignificant(final Method method) {
        return isSignificant(method.getModifiers());
    }

    private static boolean isSignificant(final int modifiers) {
        return 0 == (modifiers & NOT_SIGNIFICANT);
    }

    static class Info {

        private final Prefix prefix;
        private final Method method;
        //private final String name;

        Info(final Method method) {
            this.prefix = Stream.of(Prefix.values())
                                .filter(value -> method.getName().startsWith(value.name()))
                                .findAny()
                                .orElse(null);
            this.method = method;
        }

        final boolean isGetter() {
            return (prefix != null) && prefix.isGetter() && (0 == method.getParameterCount());
        }

        final boolean isSetter() {
            return (prefix != null) && prefix.isSetter() && (1 == method.getParameterCount());
        }

        final boolean isAccessor() {
            return isGetter() || isSetter();
        }

        final String getName() {
            final String name0 = method.getName();
            final int index0 = prefix.name().length();
            final int index1 = index0 < name0.length() ? index0 + 1 : index0;
            return name0.substring(index0, index1).toLowerCase(Locale.ROOT) + name0.substring(index1);
        }
    }

    private enum Type {
        GETTER, SETTER
    }

    private enum Prefix {
        get(Type.GETTER), is(Type.GETTER), set(Type.SETTER);

        private final Type type;

        Prefix(final Type type) {
            this.type = type;
        }

        private boolean isGetter() {
            return Type.GETTER == type;
        }

        public boolean isSetter() {
            return Type.SETTER == type;
        }
    }

    static class Collector<T> {

        private final Map<String, Property<T>> properties = new TreeMap<>();

        final void add(final Info info) {
            properties.computeIfAbsent(info.getName(), Property::new)
                      .add(info);
        }

        final void addAll(final Collector<T> other) {
            throw new UnsupportedOperationException("should not be necessary");
        }

        final Stream<de.team33.patterns.properties.e1a.Property<T>> stream() {
            return properties.values()
                             .stream()
                             .map(Property::generalize);
        }
    }

    static final class Property<T> implements de.team33.patterns.properties.e1a.Property<T> {

        private static final String CANNOT_GET_VALUE = "cannot get value gy getter from a given subject:%n" +
                "- getter: %s%n" +
                "- class of subject: %s%n" +
                "- value of subject: %s%n";

        private final String name;

        private Method getter;
        private Method setter;

        private Property(final String name) {
            this.name = name;
        }

        @Override
        public final String name() {
            return name;
        }

        @Override
        public final Object valueOf(final T subject) {
            try {
                return getter.invoke(subject);
            } catch (final IllegalAccessException | InvocationTargetException e) {
                throw new IllegalArgumentException(String.format(CANNOT_GET_VALUE, getter, subject.getClass(), subject),
                                                   e);
            }
        }

        private void add(final Info info) {
            if (info.prefix.type == Type.GETTER) {
                if (overrides(info.method, getter)) {
                    getter = info.method;
                }
            } else if (info.prefix.type == Type.SETTER) {
                if (overrides(info.method, setter)) {
                    setter = info.method;
                }
            } else {
                throw new IllegalArgumentException(String.format("cannot set method%n" +
                                                                         "- type: %s%n" +
                                                                         "- method: %s%n" +
                                                                         "- current getter: %s%n" +
                                                                         "- current setter: %s%n",
                                                                 info.prefix.type, info.method, getter, setter));
            }
        }

        private static boolean overrides(final Method actual, final Method deposited) {
            return true; //(null == deposited); //|| deposited.getDeclaringClass().isAssignableFrom(actual.getDeclaringClass());
        }

        private de.team33.patterns.properties.e1a.Property<T> generalize() {
            return this;
        }
    }
}
