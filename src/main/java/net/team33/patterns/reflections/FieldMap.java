package net.team33.patterns.reflections;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static net.team33.patterns.reflections.FieldUtil.get;
import static net.team33.patterns.reflections.FieldUtil.set;

public interface FieldMap<T> {

    /**
     * Supplies a {@link Stream} of {@link Entry Map.Entries} of provided {@link Field Fields}
     * associated with an appropriate name, that may differ from the original {@link Field#getName() Field.name}.
     *
     * The Fields must be {@linkplain Field#setAccessible(boolean) accessible}.
     */
    Stream<Entry<String, Field>> entries();

    /**
     * Supplies a {@link Mapper} for a given {@code subject}.
     */
    default Mapper<T> map(final T original) {
        return new Mapper<>(this, original);
    }

    /**
     * Supplies a string representation for a given {@code subject}.
     */
    default String toString(final T subject) {
        return entries()
                .map(entry -> entry.getKey() + "=" + get(entry.getValue(), subject))
                .collect(Collectors.joining(", ", "{", "}"));
    }

    /**
     * Represents a mapper to convert a given {@code subject} from or to a {@link Map}.
     */
    class Mapper<T> {

        private final FieldMap<T> fields;
        private final T subject;

        private Mapper(final FieldMap<T> fields, final T subject) {
            this.fields = fields;
            this.subject = subject;
        }

        /**
         * Maps all provided fields from the {@code subject} to a given {@link Map}.
         *
         * @return The {@code target}
         */
        public final Map<String, Object> to(final Map<String, Object> target) {
            fields.entries()
                    .forEach(entry -> target.put(entry.getKey(), get(entry.getValue(), subject)));
            return target;
        }

        /**
         * Maps all provided fields from a given {@link Map} to the {@code subject}.
         *
         * @return The {@code subject}
         */
        public final T from(final Map<String, Object> origin) {
            fields.entries()
                    .forEach(entry -> set(entry.getValue(), subject, origin.get(entry.getKey())));
            return subject;
        }
    }
}
