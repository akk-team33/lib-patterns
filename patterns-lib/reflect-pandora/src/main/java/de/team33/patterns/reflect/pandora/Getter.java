package de.team33.patterns.reflect.pandora;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Function;

class Getter<T> implements Function<T, Object> {

    private static final String MESSAGE = "could not apply a subject to the associated method%n" +
            "    subject ...%n" +
            "    - type                  : %s%n" +
            "    - string representation : %s%n" +
            "    method ...%n" +
            "    - string representation : %s%n";

    private final Method method;
    private final String name;

    Getter(final Method method) {
        assert 0 == method.getParameterCount();
        // - - - - - - - - - - - - - - - - - - - - - - - - - - - -
        this.method = method;
        this.name = Methods.normalName(method);
    }

    final String name() {
        return name;
    }

    final Class<?> type() {
        return method.getReturnType();
    }

    @Override
    public final Object apply(final T subject) {
        try {
            return method.invoke(subject);
        } catch (final IllegalAccessException | InvocationTargetException e) {
            final Class<?> subjectClass = (null == subject) ? null : subject.getClass();
            throw new IllegalStateException(String.format(MESSAGE, subjectClass, subject, method), e);
        }
    }
}
