package net.team33.patterns.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.TreeMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.Collections.unmodifiableMap;

@SuppressWarnings({"InstanceMethodNamingConvention", "StaticMethodNamingConvention"})
public final class Fields<T> {

    private static final Predicate<Field> FILTER = field -> {
        final int modifiers = field.getModifiers();
        return !(Modifier.isStatic(modifiers) || Modifier.isTransient(modifiers));
    };

    private final Class<T> subjectClass;
    private final Map<String, Field> backing;

    private Fields(final Class<T> subjectClass) {
        this.subjectClass = subjectClass;
        this.backing = build(subjectClass);
    }

    private static Map<String, Field> build(final Class<?> subjectClass) {
        return unmodifiableMap(build(new TreeMap<>(), subjectClass.getDeclaredFields(), subjectClass.getSuperclass()));
    }

    private static Map<String, Field> build(final Map<String, Field> result,
                                            final Field[] fields,
                                            final Class<?> superClass) {
        if (null != superClass) {
            build(result, superClass.getDeclaredFields(), superClass.getSuperclass());
        }
        for (final Field field : fields) {
            if (FILTER.test(field)) {
                field.setAccessible(true);
                result.put(field.getName(), field);
            }
        }
        return result;
    }

    public static <T> Fields<T> of(final Class<T> subjectClass) {
        return new Fields<>(subjectClass);
    }

    private static Object valueOf(final Field field, final Object subject) {
        try {
            return field.get(subject);
        } catch (final IllegalAccessException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    private static void set(final Field field, final Object subject, final Object value) {
        try {
            field.set(subject, value);
        } catch (final IllegalAccessException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    public final Mapper<T, Map<String, Object>> map(final T subject) {
        return new Mapping<>(this, subject);
    }

    public final Copying<T> copy(final T subject) {
        return new Copying<>(this, subject);
    }

    public final int hashCode(final T subject) {
        return backing.values().stream()
                .map(field -> Objects.hashCode(valueOf(field, subject)))
                .reduce(0, (x, y) -> (x >>> 7) ^ y);
    }

    public final boolean equals(final T subject, final Object other) {
        //noinspection ObjectEquality
        return (subject == other) || (
                subjectClass.isInstance(other) && matches(subject, subjectClass.cast(other))
        );
    }

    public final boolean matches(final T subject, final T other) {
        return backing.values().stream()
                .allMatch(field -> Objects.equals(
                        valueOf(field, subject),
                        valueOf(field, other)
                ));
    }

    public final String toString(final T subject) {
        return backing.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + valueOf(entry.getValue(), subject))
                .collect(Collectors.joining(", ", "{", "}"));
    }

    public interface Mapper<X, Y> {

        <Z extends Y> Z to(Z other);

        X from(Y other);
    }

    public static final class Copying<T> implements Mapper<T, T> {
        private final Fields<T> fields;
        private final T subject;

        private Copying(final Fields<T> fields, final T subject) {
            this.fields = fields;
            this.subject = subject;
        }

        private <U extends T> U copy(final T origin, final U target) {
            for (final Field field : fields.backing.values()) {
                set(field, target, valueOf(field, origin));
            }
            return target;
        }

        @Override
        public T from(final T other) {
            return copy(other, subject);
        }

        @Override
        public <U extends T> U to(final U other) {
            return copy(subject, other);
        }
    }

    public static final class Mapping<T> implements Mapper<T, Map<String, Object>> {
        private final Fields<T> fields;
        private final T subject;

        private Mapping(final Fields<T> fields, final T subject) {
            this.fields = fields;
            this.subject = subject;
        }

        @Override
        public <M extends Map<String, Object>> M to(final M other) {
            for (final Entry<String, Field> entry : fields.backing.entrySet()) {
                other.put(entry.getKey(), valueOf(entry.getValue(), subject));
            }
            return other;
        }

        @Override
        public T from(final Map<String, Object> other) {
            for (final Entry<String, Field> entry : fields.backing.entrySet()) {
                set(entry.getValue(), subject, other.get(entry.getKey()));
            }
            return subject;
        }
    }
}
