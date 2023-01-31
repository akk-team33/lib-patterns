package de.team33.patterns.reflect.luna;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class Fields {

    private static final String DUPLICATED_NAME = "" +
            "Two fields are not expected to have the same name!%n" +
            "Affected are:%n" +
            "   - %s%n" +
            "   - %s%n";
    private static final BinaryOperator<Field> DENY_DUPLICATED = (left, right) -> {
        throw new IllegalStateException(String.format(DUPLICATED_NAME, left, right));
    };
    private static final Collector<Field, ?, List<Field>> TO_LIST =
            Collectors.toList();
    public static final Collector<Field, ?, TreeMap<String, Field>> TO_MAP =
            Collectors.toMap(Field::getName, Function.identity(), DENY_DUPLICATED, TreeMap::new);

    /**
     * Returns a {@link List} of all significant instance fields declared by a given class.
     * <p>
     * "Significant instance fields" are in particular ...
     * <ul>
     *     <li>NOT static</li>
     *     <li>NOT transient</li>
     *     <li>NOT synthetic</li>
     * </ul>
     */
    public static List<Field> listOf(final Class<?> subjectClass) {
        return significantFlat(subjectClass).collect(TO_LIST);
    }

    /**
     * Returns a {@link Map} of all significant instance fields declared by a given class.
     * <p>
     * "Significant instance fields" are in particular ...
     * <ul>
     *     <li>NOT static</li>
     *     <li>NOT transient</li>
     *     <li>NOT synthetic</li>
     * </ul>
     */
    public static Map<String, Field> mapOf(final Class<?> subjectClass) {
        return significantFlat(subjectClass).collect(TO_MAP);
    }

    private static Stream<Field> significantFlat(final Class<?> subjectClass) {
        return Stream.of(subjectClass.getDeclaredFields())
                     .filter(Filter::isSignificant);
    }

    private static final class Streaming {

        private static Stream<Field> flat(final Class<?> subjectClass) {
            return Stream.of(subjectClass.getDeclaredFields());
        }

        private static Stream<Field> deep(final Class<?> subjectClass) {
            return Stream.concat(deepNullable(subjectClass.getSuperclass()), flat(subjectClass));
        }

        private static Stream<Field> deepNullable(final Class<?> superClass) {
            return (superClass == null) ? Stream.empty() : deep(superClass);
        }
    }

    public static final class Filter {

        private static final int SYNTHETIC = 0x00001000;
        private static final int NOT_SIGNIFICANT = Modifier.STATIC | Modifier.TRANSIENT | SYNTHETIC;

        public static boolean isSignificant(Field field) {
            return isSignificant(field.getModifiers());
        }

        private static boolean isSignificant(int modifiers) {
            return 0 == (modifiers & NOT_SIGNIFICANT);
        }
    }
}
