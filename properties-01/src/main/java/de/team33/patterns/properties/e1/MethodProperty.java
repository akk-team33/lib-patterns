package de.team33.patterns.properties.e1;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class MethodProperty<T> implements Property<T> {

    private static final String CANNOT_GET_VALUE = "cannot get value by getter from a given subject:%n" +
            "- getter: %s%n" +
            "- class of subject: %s%n" +
            "- value of subject: %s%n";

    private static final String CANNOT_SET_VALUE = "cannot set value by setter to a given subject:%n" +
            "- setter: %s%n" +
            "- class of subject: %s%n" +
            "- value of subject: %s%n";

    private final String name;
    private Method getter;
    private Method setter;

    MethodProperty(final String name) {
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
            throw new IllegalArgumentException(String.format(CANNOT_GET_VALUE, getter, subject.getClass(), subject), e);
        }
    }

    @Override
    public final void setValueOf(final T subject, final Object value) {
        try {
            setter.invoke(subject, value);
        } catch (final IllegalAccessException | InvocationTargetException e) {
            throw new IllegalArgumentException(String.format(CANNOT_SET_VALUE, getter, subject.getClass(), subject), e);
        }
    }

    final void set(final MethodType type, final Method method) {
        if (type.isSetter()) {
            setter = method;
        } else {
            getter = method;
        }
    }

    final Property<T> moreCommon() {
        return this;
    }
}
