package net.team33.patterns.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.function.Predicate;

import static java.util.Collections.unmodifiableMap;

@SuppressWarnings({"InstanceMethodNamingConvention", "StaticMethodNamingConvention"})
public final class Fields<T> {

    private static final Predicate<Field> FILTER = field -> {
        final int modifiers = field.getModifiers();
        return !(Modifier.isStatic(modifiers) || Modifier.isTransient(modifiers));
    };

    private final Map<String, Field> backing;

    private Fields(final Class<T> subjectClass) {
        backing = build(subjectClass);
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

    public final Mapping<T> map(final T subject) {
        return new Mapping<>(this, subject);
    }

    public final Copying<T> copy(final T subject) {
        return new Copying<>(this, subject);
    }

    public static final class Copying<T> {
        private final Fields<T> fields;
        private final T subject;

        private Copying(final Fields<T> fields, final T subject) {
            this.fields = fields;
            this.subject = subject;
        }

        private T copy(final T origin, final T target) {
            try {
                for (final Field entry : fields.backing.values()) {
                    entry.set(target, entry.get(origin));
                }
                return target;

            } catch (final IllegalAccessException caught) {
                throw new IllegalStateException(caught);
            }
        }

        public T from(final T origin) {
            return copy(origin, subject);
        }

        public T to(final T target) {
            return copy(subject, target);
        }
    }

    public static final class Mapping<T> {
        private final Fields<T> fields;
        private final T subject;

        private Mapping(final Fields<T> fields, final T subject) {
            this.fields = fields;
            this.subject = subject;
        }

        public <M extends Map<String, Object>> M to(final M target) {
            try {
                for (final Entry<String, Field> entry : fields.backing.entrySet()) {
                    target.put(entry.getKey(), entry.getValue().get(subject));
                }
                return target;

            } catch (final IllegalAccessException caught) {
                throw new IllegalStateException(caught);
            }
        }

        public T from(final Map<String, ?> origin) {
            try {
                for (final Entry<String, Field> entry : fields.backing.entrySet()) {
                    entry.getValue().set(subject, origin.get(entry.getKey()));
                }
                return subject;

            } catch (final IllegalAccessException caught) {
                throw new IllegalStateException(caught);
            }
        }
    }
}
