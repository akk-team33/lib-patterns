package net.team33.patterns.reflections;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static net.team33.patterns.reflections.FieldUtil.get;
import static net.team33.patterns.reflections.FieldUtil.set;

@FunctionalInterface
public interface FieldMap<T> {

    /**
     * Supplies a {@link Stream} of {@link Entry Entries} over the provided {@link Field Fields}
     * associated with an appropriate name, that may differ from the original {@link Field#getName() Field.name}.
     *
     * The Fields must be {@linkplain Field#setAccessible(boolean) accessible}.
     */
    Stream<Entry> entries();

    /**
     * Supplies a {@link Mapper} for a given {@code subject}.
     */
    default Mapper<T> map(final T subject) {
        return new Mapper<T>() {
            @Override
            public Map<String, Object> to(final Map<String, Object> target) {
                entries().forEach(entry -> target.put(entry.name(), get(entry.field(), subject)));
                return target;
            }

            @Override
            public T from(final Map<String, Object> origin) {
                entries().forEach(entry -> set(entry.field(), subject, origin.get(entry.name())));
                return subject;
            }
        };
    }

    /**
     * Supplies a string representation for a given {@code subject}.
     */
    default String toString(final T subject) {
        return entries()
                .map(entry -> entry.name() + "=" + get(entry.field(), subject))
                .collect(Collectors.joining(", ", "{", "}"));
    }

    /**
     * Represents an Entry of the FieldMap.
     */
    interface Entry {

        /**
         * The represented {@link Field}.
         */
        Field field();

        /**
         * The associated name (may differ from {@link Field#getName()}).
         */
        String name();
    }

    /**
     * Represents a mapper to convert a given {@code subject} from or to a {@link Map}.
     */
    interface Mapper<T> {

        /**
         * Maps all provided fields from the {@code subject} to a given {@link Map}.
         *
         * @return The {@code target}
         */
        Map<String, Object> to(final Map<String, Object> target);

        /**
         * Maps all provided fields from a given {@link Map} to the {@code subject}.
         *
         * @return The {@code subject}
         */
        T from(final Map<String, Object> origin);
    }
}
