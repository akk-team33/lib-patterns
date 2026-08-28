package de.team33.patterns.records.rho;

import de.team33.patterns.typing.proteus.Type;

import java.util.Map;

public final class Records {

    private Records() {}

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
