package net.team33.patterns.reflections;

import net.team33.patterns.reflect.fields.Filter;
import net.team33.patterns.reflect.fields.ToName;
import net.team33.patterns.reflect.fields.ToStream;

import java.lang.reflect.Field;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Collections.unmodifiableMap;

public class Fields<T> {

    private final Map<String, Field> backing;

    private Fields(final Stream<Field> fields, final Function<Field, String> fieldToName) {
        backing = unmodifiableMap(fields.collect(Collectors.toMap(fieldToName, field -> field)));
    }

    public static <T> Fields<T> of(final Class<T> subjectClass) {
        return builder(subjectClass).build();
    }

    private static <T> Builder<T> builder(final Class<T> subjectClass) {
        return new Builder<>(subjectClass);
    }

    public final Proxy toMap(final T subject) {
        return new Proxy(subject);
    }

    @SuppressWarnings({"unused", "FieldHasSetterButNoGetter"})
    public static class Builder<T> {

        private final Class<T> subjectClass;
        private Function<Class<?>, Stream<Field>> classToFields = ToStream.FLAT;
        private Predicate<Field> filter = Filter.SIGNIFICANT;
        private Function<Field, String> fieldToName = ToName.SIMPLE;

        private Builder(final Class<T> subjectClass) {
            this.subjectClass = subjectClass;
        }

        public final Fields<T> build() {
            return new Fields<>(
                    classToFields.apply(subjectClass)
                            .filter(filter)
                            .peek(field -> field.setAccessible(true)),
                    fieldToName
            );
        }

        public final Builder<T> setClassToFields(final Function<Class<?>, Stream<Field>> classToFields) {
            this.classToFields = classToFields;
            return this;
        }

        public final Builder<T> setFieldToName(final Function<Field, String> fieldToName) {
            this.fieldToName = fieldToName;
            return this;
        }
    }

    public final class Proxy extends AbstractMap<String, Object> {
        private final T subject;
        private final Set<Entry<String, Object>> entries = new EntrySet();

        private Proxy(final T subject) {
            this.subject = subject;
        }

        @Override
        public final Set<Entry<String, Object>> entrySet() {
            return entries;
        }

        public final T adopt(final Map<String, ?> origin) {
            backing.forEach((key, value) -> {
                try {
                    value.set(subject, origin.get(key));
                } catch (final IllegalAccessException caught) {
                    throw new IllegalArgumentException("cannot adopt [" + key + "] from " + origin, caught);
                }
            });
            return subject;
        }

        private class EntryIterator implements Iterator<Entry<String, Object>> {
            private final Iterator<Entry<String, Field>> core;

            private EntryIterator(final Iterator<Entry<String, Field>> core) {
                this.core = core;
            }

            @Override
            public final boolean hasNext() {
                return core.hasNext();
            }

            @Override
            public final Entry<String, Object> next() {
                final Entry<String, Field> entry = core.next();
                final String key = entry.getKey();
                try {
                    return new SimpleImmutableEntry<>(key, entry.getValue().get(subject));
                } catch (final IllegalAccessException caught) {
                    throw new IllegalStateException("cannot get value of [" + key + "]", caught);
                }
            }
        }

        private class EntrySet extends AbstractSet<Entry<String, Object>> {

            @Override
            public final Iterator<Entry<String, Object>> iterator() {
                return new EntryIterator(backing.entrySet().iterator());
            }

            @Override
            public final int size() {
                return backing.size();
            }
        }
    }
}
