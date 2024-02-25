package de.team33.patterns.testing.reflect.pandora;

import java.lang.reflect.Field;
import java.time.Instant;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Stream;

public class RecordClass {

    private final int intValue;
    private final Long longValue;
    private final String stringValue;
    private final Instant instantValue;

    public RecordClass(final int intValue, final Long longValue, final String stringValue, final Instant instantValue) {
        this.intValue = intValue;
        this.longValue = longValue;
        this.stringValue = stringValue;
        this.instantValue = instantValue;
    }

    public final int intValue() {
        return intValue;
    }

    @SuppressWarnings("ParameterHidesMemberVariable")
    public final RecordClass intValue(final int intValue) {
        return new RecordClass(intValue, longValue, stringValue, instantValue);
    }

    public final Long longValue() {
        return longValue;
    }

    @SuppressWarnings("ParameterHidesMemberVariable")
    public final RecordClass longValue(final Long longValue) {
        return new RecordClass(intValue, longValue, stringValue, instantValue);
    }

    public final String stringValue() {
        return stringValue;
    }

    @SuppressWarnings("ParameterHidesMemberVariable")
    public final RecordClass stringValue(final String stringValue) {
        return new RecordClass(intValue, longValue, stringValue, instantValue);
    }

    public final Instant instantValue() {
        return instantValue;
    }

    @SuppressWarnings("ParameterHidesMemberVariable")
    public final RecordClass instantValue(final Instant instantValue) {
        return new RecordClass(intValue, longValue, stringValue, instantValue);
    }

    private Map<String, Object> toMap() {
        return Stream.of(getClass().getDeclaredFields())
                     .collect(TreeMap::new, this::put, Map::putAll);
    }

    private void put(final Map<String, Object> map, final Field field) {
        try {
            map.put(field.getName(), field.get(this));
        } catch (final IllegalAccessException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public final int hashCode() {
        return toMap().hashCode();
    }

    @Override
    public final boolean equals(final Object obj) {
        return (this == obj) || ((obj instanceof RecordClass) && toMap().equals(((RecordClass) obj).toMap()));
    }

    @Override
    public final String toString() {
        return toMap().toString();
    }
}
