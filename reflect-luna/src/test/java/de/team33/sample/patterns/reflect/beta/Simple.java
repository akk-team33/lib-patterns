package de.team33.sample.patterns.reflect.beta;

import de.team33.patterns.reflect.beta.Fields;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"FieldNotUsedInToString", "CopyConstructorMissesField"})
public class Simple {

    private static final Fields<Simple> FIELDS = Fields.of(Simple.class,
                                                           (source, field) -> field.get(source));

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
        return FIELDS.toList(this);
    }

    public final Map<String, Object> toMap() {
        return FIELDS.toMap(this);
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
