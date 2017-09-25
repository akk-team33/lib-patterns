package net.team33.patterns.reflect;

import net.team33.patterns.reflect.fields.Filter;
import net.team33.patterns.reflect.fields.Modder;

import java.lang.reflect.Field;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

public class FieldMap extends AbstractMap<String, Object> {

    private final Set<Entry<String, Object>> entries = new EntrySet();
    private final Map<String, Field> fields;
    private final Object subject;

    private FieldMap(final Map<String, Field> fields, final Object subject) {
        this.fields = fields;
        this.subject = subject;
    }

    public static <T> Builder<T> builder(final Class<T> sampleClass) {
        return new Builder<>(sampleClass);
    }

    @Override
    public final Set<Entry<String, Object>> entrySet() {
        return entries;
    }

    @Override
    public final Object put(final String key, final Object value) {
        final Field field = fields.get(key);
        final Object result = FieldUtil.get(field, subject);
        FieldUtil.set(field, subject, value);
        return result;
    }

    @SuppressWarnings("FieldHasSetterButNoGetter")
    public static class Builder<T> {

        private final Class<T> subjectClass;
        private Function<Class<?>, Stream<Field>> toStream = FieldsToStream.SIMPLE;
        private Predicate<Field> filter = Filter.SIGNIFICANT;
        private Function<Field, String> toName = Field::getName;

        private Builder(final Class<T> subjectClass) {
            this.subjectClass = subjectClass;
        }

        public final Function<T, FieldMap> build() {
            final Map<String, Field> fields = toStream.apply(subjectClass)
                    .filter(filter)
                    .peek(Modder.SET_ACCESSIBLE)
                    .collect(toMap(toName, field -> field));
            return subject -> new FieldMap(fields, subject);
        }

        public final Builder<T> setToStream(final Function<Class<?>, Stream<Field>> toStream) {
            this.toStream = toStream;
            return this;
        }

        public final Builder<T> setFilter(final Predicate<Field> filter) {
            this.filter = filter;
            return this;
        }

        public final Builder<T> setToName(final Function<Field, String> toName) {
            this.toName = toName;
            return this;
        }

        public final Builder<T> setToNameByClass(final Function<Class<?>, Function<Field, String>> toNameByClass) {
            this.toName = toNameByClass.apply(subjectClass);
            return this;
        }
    }

    private final class EntryIterator implements Iterator<Entry<String, Object>> {
        private final Iterator<Entry<String, Field>> backing;

        private EntryIterator(final Iterator<Entry<String, Field>> backing) {
            this.backing = backing;
        }

        @Override
        public final boolean hasNext() {
            return backing.hasNext();
        }

        @Override
        public Entry<String, Object> next() {
            final Entry<String, Field> entry = backing.next();
            return new SimpleEntry<>(entry.getKey(), FieldUtil.get(entry.getValue(), subject));
        }
    }

    private final class EntrySet extends AbstractSet<Entry<String, Object>> {
        @Override
        public Iterator<Entry<String, Object>> iterator() {
            return new EntryIterator(fields.entrySet().iterator());
        }

        @Override
        public int size() {
            return fields.size();
        }
    }
}
