package de.team33.patterns.reflect.pandora.testing;

import java.lang.reflect.Field;
import java.time.Instant;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Stream;

public class BeanClass implements BeanInterface {

    private int intValue;
    private Long longValue;
    private String stringValue;
    private Instant instantValue;

    @Override
    public int getIntValue() {
        return intValue;
    }

    public BeanClass setIntValue(int intValue) {
        this.intValue = intValue;
        return this;
    }

    @Override
    public Long getLongValue() {
        return longValue;
    }

    public BeanClass setLongValue(Long longValue) {
        this.longValue = longValue;
        return this;
    }

    @Override
    public String getStringValue() {
        return stringValue;
    }

    public BeanClass setStringValue(String stringValue) {
        this.stringValue = stringValue;
        return this;
    }

    @Override
    public Instant getInstantValue() {
        return instantValue;
    }

    public BeanClass setInstantValue(Instant instantValue) {
        this.instantValue = instantValue;
        return this;
    }

    private Map<String, Object> toMap() {
        return Stream.of(getClass().getFields())
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
        return this == obj || (obj instanceof BeanClass && toMap().equals(((BeanClass)obj).toMap()));
    }

    @Override
    public final String toString() {
        return toMap().toString();
    }
}
