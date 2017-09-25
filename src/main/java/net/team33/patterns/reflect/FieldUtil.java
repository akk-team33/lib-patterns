package net.team33.patterns.reflect;

import java.lang.reflect.Field;

public final class FieldUtil {

    private FieldUtil() {
    }

    /**
     * Retrieves the value of the specified {@code field} of a given {@code target}
     * wrapping an {@link IllegalAccessException} as {@link IllegalStateException}.
     *
     * @see Field#get(Object)
     */
    public static Object get(final Field field, final Object subject) {
        try {
            return field.get(subject);
        } catch (final IllegalAccessException caught) {
            throw new IllegalStateException(caught.getMessage(), caught);
        }
    }

    /**
     * Sets the specified {@code field} on a given {@code target} to a given {@code value}
     * wrapping an {@link IllegalAccessException} as {@link IllegalStateException}.
     *
     * @see Field#set(Object, Object)
     */
    public static void set(final Field field, final Object target, final Object value) {
        try {
            field.set(target, value);
        } catch (final IllegalAccessException caught) {
            throw new IllegalStateException(caught.getMessage(), caught);
        }
    }
}
