package net.team33.patterns.reflect.fields;

import java.lang.reflect.Field;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Provides some {@linkplain Function methods} to get a name from and for a given {@link Field}.
 */
public final class ToName {

    /**
     * Defines a {@link Function} to get the simple name of a {@link Field}.
     */
    public static final Function<Field, String> SIMPLE = Field::getName;

    /**
     * Defines a {@link Function} to get the full qualified name of a {@link Field}.
     */
    public static final Function<Field, String> QUALIFIED = field ->
            field.getDeclaringClass().getCanonicalName() + "." + field.getName();

    /**
     * Defines a {@link Function} to get a {@link Function} to get a prefixed name of a {@link Field}.
     *
     * The choice of the prefix depends on a given {@link Class} which is assumed to be the topmost class
     * within a class-hierarchy from which the fields are derived.
     */
    public static final Function<Class<?>, Function<Field, String>> PREFIXED =
            aClass -> field -> Stream.generate(() -> ".")
                    .limit(distance(aClass, field.getDeclaringClass()))
                    .collect(Collectors.joining()) + field.getName();

    private ToName() {
    }

    private static int distance(final Class<?> aClass, final Class<?> superClass) {
        //noinspection ObjectEquality
        return (aClass == superClass) ? 0 : (1 + distance(aClass.getSuperclass(), superClass));
    }
}
