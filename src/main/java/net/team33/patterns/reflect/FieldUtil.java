package net.team33.patterns.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public final class FieldUtil {

    /**
     * Defines a filter accepting all but static or transient fields.
     * Those fields should be significant for a type with value semantics.
     */
    public static final Predicate<Field> SIGNIFICANT = field -> {
        final int modifiers = field.getModifiers();
        return !(Modifier.isStatic(modifiers) || Modifier.isTransient(modifiers));
    };

    /**
     * Defines a {@link Consumer} to make a {@link Field} {@linkplain Field#setAccessible(boolean) accessible}.
     */
    public static final Consumer<Field> SET_ACCESSIBLE = field -> field.setAccessible(true);

    /**
     * Defines a {@link Function} to make (as a side-effect) a {@link Field}
     * {@linkplain Field#setAccessible(boolean) accessible}.
     */
    public static final Function<Field, Field> ACCESSIBLE = field -> {
        field.setAccessible(true);
        return field;
    };

    /**
     * Defines a {@link Function} to get the simple name of a {@link Field}.
     */
    public static final Function<Field, String> TO_SIMPLE_NAME = Field::getName;

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
