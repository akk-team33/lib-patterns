package de.team33.patterns.reflect.pandora;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.BiConsumer;

import static java.lang.String.format;

class Setter<T> implements BiConsumer<T, Object> {

    private static final String MESSAGE = "could not apply (subject, value) to the associated method%n" +
            "    subject ...%n" +
            "    - type                  : %s%n" +
            "    - string representation : %s%n" +
            "    value ...%n" +
            "    - type                  : %s%n" +
            "    - string representation : %s%n" +
            "    method ...%n" +
            "    - string representation : %s%n";

    private final Method method;
    private final String name;
    private final Class<?> type;

    Setter(final Method method) {
        assert 1 == method.getParameterCount();
        // - - - - - - - - - - - - - - - - - - - - - - - - - - - -
        this.method = method;
        this.name = Methods.normalName(method);
        this.type = method.getParameterTypes()[0];
    }

    final String name() {
        return name;
    }

    final Class<?> type() {
        return type;
    }

    @Override
    public final void accept(final T subject, final Object value) {
        try {
            method.invoke(subject, value);
        } catch (final IllegalAccessException | InvocationTargetException e) {
            final Class<?> subjectClass = (null == subject) ? null : subject.getClass();
            final Class<?> valueClass = (null == value) ? null : value.getClass();
            throw new IllegalStateException(format(MESSAGE, subjectClass, subject, valueClass, value, method), e);
        }
    }
}
