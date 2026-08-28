package de.team33.patterns.records.metis;

import de.team33.patterns.typing.proteus.Type;

import java.util.Map;

/**
 * A utility that provides conversions between {@linkplain java.lang.Record Records}
 * and {@linkplain java.util.Map Map} representations.
 */
public final class Metis {

    private Metis() {}

    public static <T extends Record> Map<String, Class<?>> description(final Class<T> recordClass) {
        return Reflector.of(recordClass).description();
    }

    public static <T extends Record> Map<String, Type<?>> description(final Type<T> recordType) {
        return Refractor.of(recordType).description();
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static Map<String, Object> toMap(final Record source) {
        return Reflector.of((Class) source.getClass())
                        .toMap(source);
    }

    public static <T extends Record> T toRecord(final Class<T> recordClass, final Map<String, Object> source) {
        return Reflector.of(recordClass)
                        .toRecord(source);
    }

    public static <T extends Record> T toRecord(final Type<T> recordType, final Map<String, Object> source) {
        return Refractor.of(recordType)
                        .toRecord(source);
    }
}
