package de.team33.patterns.reflect.pandora.testing;

import java.lang.reflect.Field;
import java.time.Instant;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Stream;

public class MixedClass implements BeanInterface {

    private int intValue;
    private Long longValue;
    private String stringValue;
    private Instant instantValue;

    public final int intValue() {
        return intValue;
    }

    @Override
    public final int getIntValue() {
        return intValue;
    }

    public final MixedClass setIntValue(final int intValue) {
        this.intValue = intValue;
        return this;
    }

    public final Long longValue() {
        return longValue;
    }

    @Override
    public final Long getLongValue() {
        return longValue;
    }

    public final MixedClass setLongValue(final Long longValue) {
        this.longValue = longValue;
        return this;
    }

    public final String stringValue() {
        return stringValue;
    }

    @Override
    public final String getStringValue() {
        return stringValue;
    }

    public final MixedClass setStringValue(final String stringValue) {
        this.stringValue = stringValue;
        return this;
    }

    public final Instant instantValue() {
        return instantValue;
    }

    @Override
    public final Instant getInstantValue() {
        return instantValue;
    }

    public final MixedClass setInstantValue(final Instant instantValue) {
        this.instantValue = instantValue;
        return this;
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
        return this == obj || (obj instanceof MixedClass && toMap().equals(((MixedClass)obj).toMap()));
    }

    @Override
    public final String toString() {
        return toMap().toString();
    }
}
