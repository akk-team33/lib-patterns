package net.team33.patterns.reflections;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.function.BinaryOperator;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

public class Fields {

    private static final Predicate<Field> FILTER = field -> {
        final int modifiers = field.getModifiers();
        return !(Modifier.isStatic(modifiers) || Modifier.isTransient(modifiers));
    };
    private static final BinaryOperator<Field> MERGE = (x, y) -> {
        throw new IllegalStateException("duplicate name: " + x.getName());
    };

    private final Class<?> subjectClass;
    private final Map<String, Field> backing;

    private Fields(final Class<?> subjectClass) {
        this.subjectClass = subjectClass;
        final Field[] fields = subjectClass.getDeclaredFields();
        this.backing = newFieldMap(fields);
    }

    private static Map<String, Field> newFieldMap(final Field[] fields) {
        return Stream.of(fields).filter(FILTER)
                .collect(toMap(Field::getName, field -> {
                    field.setAccessible(true);
                    return field;
                }, MERGE, TreeMap::new));
    }

    /**
     * Retrieves a new instance of {@link Fields}.
     */
    public static Fields of(final Class<?> subjectClass) {
        return new Fields(subjectClass);
    }

    /**
     * Retrieves the value of the specified {@code field} of a given {@code target}
     * wraping an {@link IllegalAccessException} as {@link IllegalArgumentException}.
     *
     * @see Field#get(Object)
     */
    public static Object get(final Field field, final Object subject) {
        try {
            return field.get(subject);
        } catch (final IllegalAccessException caught) {
            throw new IllegalArgumentException(caught.getMessage(), caught);
        }
    }

    /**
     * Sets the specified {@code field} on a given {@code target} to a given {@code value}
     * wraping an {@link IllegalAccessException} as {@link IllegalArgumentException}.
     *
     * @see Field#set(Object, Object)
     */
    public static void set(final Field field, final Object target, final Object value) {
        try {
            field.set(target, value);
        } catch (final IllegalAccessException caught) {
            throw new IllegalArgumentException(caught.getMessage(), caught);
        }
    }

    /**
     * Copies all relevant fields from an {@code original} to a {@code target}.
     *
     * @return The {@code target}
     * @throws IllegalArgumentException When {@code original} or {@code target} cannot be handled by this Fields
     */
    public final <T> T copy(final Object original, final T target) {
        backing.values().stream()
                .forEach(field -> set(field, target, get(field, original)));
        return target;
    }

    /**
     * Retrieves a {@link Mapping} for a given {@code subject}.
     */
    public final <T> Mapping<T> map(final T subject) {
        return new Mapping<>(this, subject);
    }

    /**
     * Determines if all values of this fields are equal between two subjects.
     *
     * @throws IllegalArgumentException When {@code left} or {@code right} cannot be handled by this Fields
     */
    public final boolean matching(final Object left, final Object right) {
        return backing.values().stream()
                .allMatch(field -> Objects.equals(get(field, left), get(field, right)));
    }

    /**
     * Retrieves a hash code for a given {@code subject}.
     *
     * @throws IllegalArgumentException When {@code subject} cannot be handled by this Fields
     */
    public final int hash(final Object subject) {
        return backing.values().stream()
                .map(field -> Objects.hashCode(get(field, subject)))
                .reduce(0, (x, y) -> (x >>> 7) ^ y);
    }

    /**
     * Retrieves a string representation for a given {@code subject}.
     *
     * @throws IllegalArgumentException When {@code subject} cannot be handled by this Fields
     */
    public final String toString(final Object subject) {
        return backing.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + get(entry.getValue(), subject))
                .collect(Collectors.joining(", ", "{", "}"));
    }

    /**
     * Represents a mapping for a specific {@code subject}.
     */
    public static final class Mapping<T> {
        private final Fields fields;
        private final T subject;

        private Mapping(final Fields fields, final T subject) {
            this.fields = fields;
            this.subject = subject;
        }

        /**
         * Maps all relevant fields from the {@code subject} to a {@code target}.
         *
         * @return The {@code target}
         * @throws IllegalArgumentException When {@code subject} cannot be handled by the underlying Fields
         */
        public final Map<String, Object> to(final Map<String, Object> target) {
            fields.backing.entrySet().stream()
                    .forEach(entry -> target.put(entry.getKey(), get(entry.getValue(), subject)));
            return target;
        }

        /**
         * Maps all relevant fields from an {@code original} to the {@code subject}.
         *
         * @return The {@code subject}
         * @throws IllegalArgumentException When {@code subject} cannot be handled by the underlying Fields
         */
        public final T from(final Map<String, Object> original) {
            fields.backing.entrySet().stream()
                    .forEach(entry -> set(entry.getValue(), subject, original.get(entry.getKey())));
            return subject;
        }
    }
}
