package de.team33.patterns.properties.e1a;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Stream;

final class Methods {

    private static final int NOT_SIGNIFICANT = Modifier.STATIC | Modifier.NATIVE;

    private Methods() {
    }

    static boolean isSignificant(final Method method) {
        return isSignificant(method.getModifiers());
    }

    private static boolean isSignificant(final int modifiers) {
        return 0 == (modifiers & NOT_SIGNIFICANT);
    }

    static class Details {

        private final Prefix prefix;
        private final Method method;

        Details(final Method method) {
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

        final String getPropertyName() {
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

        private final Map<String, Builder<T>> properties = new TreeMap<>();

        Collector(final Class<T> ignored) {
        }

        final void add(final Details details) {
            properties.computeIfAbsent(details.getPropertyName(), Builder::new)
                      .add(details);
        }

        final void addAll(final Collector<T> other) {
            throw new UnsupportedOperationException("should not be necessary");
        }

        final Stream<Property<T>> stream() {
            return properties.values()
                             .stream()
                             .map(Builder::build);
        }
    }

    static final class Builder<T> {

        private final String name;
        private Method getter;
        private Method setter;

        Builder(final String name) {
            this.name = name;
        }

        private static boolean overrides(final Method actual, final Method deposited) {
            return (null == deposited); //|| deposited.getDeclaringClass().isAssignableFrom(actual.getDeclaringClass());
        }

        final Property<T> build() {
            return new Accessor<T>(this);
        }

        final void add(final Details details) {
            if (details.prefix.type == Type.GETTER) {
                if (overrides(details.method, getter)) {
                    getter = details.method;
                }
            } else if (details.prefix.type == Type.SETTER) {
                if (overrides(details.method, setter)) {
                    setter = details.method;
                }
            } else {
                throw new IllegalArgumentException(String.format("cannot set method%n" +
                                                                         "- type: %s%n" +
                                                                         "- method: %s%n" +
                                                                         "- current getter: %s%n" +
                                                                         "- current setter: %s%n",
                                                                 details.prefix.type, details.method, getter, setter));
            }
        }
    }

    static final class Accessor<T> implements Property<T> {

        private static final String CANNOT_GET_VALUE = "cannot get value by getter from a given subject:%n" +
                "- getter: %s%n" +
                "- class of subject: %s%n" +
                "- value of subject: %s%n";

        private static final String CANNOT_SET_VALUE = "cannot set value by setter to a given subject:%n" +
                "- setter: %s%n" +
                "- value: %s%n" +
                "- class of subject: %s%n" +
                "- value of subject: %s%n";

        private final String name;
        private final Method getter;
        private final Method setter;

        Accessor(final Builder<T> builder) {
            name = builder.name;
            getter = builder.getter;
            setter = builder.setter;
        }

        @Override
        public final String name() {
            return name;
        }

        @Override
        public final Object valueOf(final T subject) {
            try {
                //noinspection ReturnOfNull
                return (null == getter) ? null : getter.invoke(subject);
            } catch (final IllegalAccessException | InvocationTargetException e) {
                throw new IllegalArgumentException(
                        String.format(CANNOT_GET_VALUE, getter, subject.getClass(), subject), e);
            }
        }

        @Override
        public final void setValue(final T subject, final Object value) {
            if (null != setter) {
                try {
                    setter.invoke(subject, value);
                } catch (final IllegalAccessException | InvocationTargetException e) {
                    throw new IllegalArgumentException(
                            String.format(CANNOT_SET_VALUE, setter, value, subject.getClass(), subject), e);
                }
            }
        }
    }
}
