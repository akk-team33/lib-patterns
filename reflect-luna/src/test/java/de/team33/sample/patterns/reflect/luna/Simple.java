package de.team33.sample.patterns.reflect.luna;

import de.team33.patterns.reflect.luna.Fields;

import java.lang.reflect.Field;
import java.time.Instant;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SuppressWarnings({"FieldNotUsedInToString", "CopyConstructorMissesField"})
public class Simple {

    private static final Fields FIELDS = Fields.of(Simple.class);

    private int intValue;
    private Double doubleValue;
    private Instant instantValue;
    private String stringValue;

    public Simple() {
    }

    public Simple(final Simple source) {
        FIELDS.forEach(field -> field.set(this, field.get(source)));
    }

    public final int getIntValue() {
        return intValue;
    }

    public final Simple setIntValue(final int intValue) {
        this.intValue = intValue;
        return this;
    }

    public final Double getDoubleValue() {
        return doubleValue;
    }

    public final Simple setDoubleValue(final Double doubleValue) {
        this.doubleValue = doubleValue;
        return this;
    }

    public final Instant getInstantValue() {
        return instantValue;
    }

    public final Simple setInstantValue(final Instant instantValue) {
        this.instantValue = instantValue;
        return this;
    }

    public final String getStringValue() {
        return stringValue;
    }

    public final Simple setStringValue(final String stringValue) {
        this.stringValue = stringValue;
        return this;
    }

    public final List<Object> toList() {
        return FIELDS.map(field -> field.get(this)).collect(Collectors.toList());
    }

    public final Map<String, Object> toMap() {
        return FIELDS.map(this::newEntry)
                     .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Map.Entry<String, Object> newEntry(final Field field) throws IllegalAccessException {
        return new AbstractMap.SimpleEntry<>(field.getName(), field.get(this));
    }

    @Override
    public final boolean equals(final Object obj) {
        return (this == obj) || ((obj instanceof Simple) && toList().equals(((Simple) obj).toList()));
    }

    @Override
    public final int hashCode() {
        return toList().hashCode();
    }

    @Override
    public final String toString() {
        return toList().toString();
    }
}
