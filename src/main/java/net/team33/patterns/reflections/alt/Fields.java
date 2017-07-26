package net.team33.patterns.reflections.alt;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public class Fields<T> {

    private final Map<String, Field> backing;

    private Fields(final Class<?> tClass,
                   final Predicate<Field> filter,
                   final Function<Field, String> toKey) {
        this.backing = Collections.unmodifiableMap(Basics.map(tClass, filter, toKey));
    }

    /**
     * Retrieves a new instance of {@link Fields}.
     */
    public static <T> Fields<T> of(final Class<T> tClass) {
        return new Fields<>(tClass, Filter.STANDARD, ToKey.SIMPLE);
    }

    /**
     * Retrieves a new instance of {@link Fields}.
     */
    public static <T> Fields<T> of(final Class<T> tClass, final Function<Field, String> toKey) {
        return new Fields<>(tClass, Filter.STANDARD, toKey);
    }

    /**
     * Copies all relevant fields from an {@code original} to a {@code target}.
     *
     * @return The {@code target}
     * @throws IllegalArgumentException When {@code original} or {@code target} cannot be handled by this Fields
     */
    public final T copy(final T original, final T target) {
        backing.values().stream()
                .forEach(field -> Basics.set(field, target, Basics.get(field, original)));
        return target;
    }

    /**
     * Retrieves a {@link Mapping} for a given {@code subject}.
     */
    public final Mapping<T> map(final T subject) {
        return new Mapping<>(this, subject);
    }

    /**
     * Determines if all values of this fields are equal between two subjects.
     *
     * @throws IllegalArgumentException When {@code left} or {@code right} cannot be handled by this Fields
     */
    public final boolean matching(final T left, final T right) {
        return backing.values().stream()
                .allMatch(field -> Objects.equals(Basics.get(field, left), Basics.get(field, right)));
    }

    /**
     * Retrieves a hash code for a given {@code subject}.
     *
     * @throws IllegalArgumentException When {@code subject} cannot be handled by this Fields
     */
    public final int hash(final T subject) {
        return backing.values().stream()
                .map(field -> Objects.hashCode(Basics.get(field, subject)))
                .reduce(0, (x, y) -> (x >>> 7) ^ y);
    }

    /**
     * Retrieves a string representation for a given {@code subject}.
     *
     * @throws IllegalArgumentException When {@code subject} cannot be handled by this Fields
     */
    public final String toString(final T subject) {
        return backing.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + Basics.get(entry.getValue(), subject))
                .collect(Collectors.joining(", ", "{", "}"));
    }

    @SuppressWarnings("unused")
    public enum ToKey implements Function<Field, String> {

        /**
         * Defines a function to get a key simply from field name.
         */
        SIMPLE {
            @Override
            public String apply(final Field field) {
                return field.getName();
            }
        },

        /**
         * Defines a function to get a key from field name prefixed by the simple name of the declaring class.
         */
        PREFIXED {
            @Override
            public String apply(final Field field) {
                return field.getDeclaringClass().getSimpleName() + "." + field.getName();
            }
        },

        /**
         * Defines a function to get a key from field name prefixed by the full qualified name of the declaring class.
         */
        FULL_QUALIFIED {
            @Override
            public String apply(final Field field) {
                return field.getDeclaringClass().getCanonicalName() + "." + field.getName();
            }
        }
    }

    @SuppressWarnings("unused")
    public enum Filter implements Predicate<Field> {

        /**
         * Defines a standard filter accepting all but static or transient fields.
         */
        STANDARD {
            @Override
            public boolean test(final Field field) {
                final int modifiers = field.getModifiers();
                return !(Modifier.isStatic(modifiers) || Modifier.isTransient(modifiers));
            }
        },

        /**
         * Defines a filter accepting all but static fields.
         */
        NON_STATIC {
            @Override
            public boolean test(final Field field) {
                final int modifiers = field.getModifiers();
                return !Modifier.isStatic(modifiers);
            }
        }
    }

    /**
     * Represents a mapping for a specific {@code subject}.
     */
    public static final class Mapping<T> {
        private final Fields<T> fields;
        private final T subject;

        private Mapping(final Fields<T> fields, final T subject) {
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
                    .forEach(entry -> target.put(entry.getKey(), Basics.get(entry.getValue(), subject)));
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
                    .forEach(entry -> Basics.set(entry.getValue(), subject, original.get(entry.getKey())));
            return subject;
        }
    }

    public static final class Basics {

        private static final BinaryOperator<Field> MERGE = (left, right) -> {
            return right;
            //throw new IllegalStateException("duplucate key: " + left + " / " + right);
        };

        private Basics() {
        }

        /**
         * Retrieves the value of the specified {@code field} of a given {@code target}
         * wrapping an {@link IllegalAccessException} as {@link IllegalArgumentException}.
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
         * wrapping an {@link IllegalAccessException} as {@link IllegalArgumentException}.
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
         * Retrieves a {@link Map} of all {@link Field}s declared by a given {@link Class} or its superclasses that are
         * matching {@link Filter#STANDARD}.
         *
         * Fields (of a superclass) may be omitted depending on a given {@code toKey}-{@link Function}.
         */
        public static Map<String, Field> map(final Class<?> subject,
                                             final Function<Field, String> toKey) {
            return map(subject, Filter.STANDARD, toKey);
        }

        /**
         * Retrieves a {@link Map} of all {@link Field}s declared by a given {@link Class} or its superclasses that are
         * matching {@link Filter#STANDARD}.
         *
         * Fields of a superclass are omitted when there are name-clashes with subclass-fields.
         */
        public static Map<String, Field> map(final Class<?> subject) {
            return map(subject, Filter.STANDARD, ToKey.SIMPLE);
        }

        /**
         * Retrieves a {@link Map} of all {@link Field}s declared by a given {@link Class} or its superclasses that are
         * matching a given filter.
         *
         * Fields of a superclass are omitted when there are name-clashes with subclass-fields.
         */
        public static Map<String, Field> map(final Class<?> subject,
                                             final Predicate<Field> filter) {
            return map(subject, filter, ToKey.SIMPLE);
        }

        /**
         * Retrieves a {@link Map} of all {@link Field}s declared by a given {@link Class} or its superclasses that are
         * matching a given filter.
         *
         * Fields (of a superclass) may be omitted depending on a given {@code toKey}-{@link Function}.
         */
        public static Map<String, Field> map(final Class<?> subject,
                                             final Predicate<Field> filter,
                                             final Function<Field, String> toKey) {
            return stream(subject).filter(filter).collect(toMap(toKey, Function.identity(), MERGE, TreeMap::new));
        }

        /**
         * Retrieves a {@link List} of all {@link Field}s declared by a given {@link Class} or its superclasses that are
         * matching {@link Filter#STANDARD}.
         */
        public static List<Field> list(final Class<?> subject) {
            return list(subject, Filter.STANDARD);
        }

        /**
         * Retrieves a {@link List} of all {@link Field}s declared by a given {@link Class} or its superclasses that are
         * matching a given filter.
         */
        public static List<Field> list(final Class<?> subject, final Predicate<Field> filter) {
            return stream(subject).filter(filter).collect(toList());
        }

        /**
         * Retrieves a {@link Stream} of all {@link Field}s declared by a given {@link Class} or its superclasses.
         */
        public static Stream<Field> stream(final Class<?> subject) {
            return (null == subject)
                    ? Stream.empty()
                    : stream(subject.getDeclaredFields(), subject.getSuperclass());
        }

        private static Stream<Field> stream(final Field[] fields, final Class<?> superclass) {
            return Stream.concat(stream(superclass), Stream.of(fields).map(field -> {
                field.setAccessible(true);
                return field;
            }));
        }
    }
}
