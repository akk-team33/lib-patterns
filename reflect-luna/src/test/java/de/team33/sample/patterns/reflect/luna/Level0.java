package de.team33.sample.patterns.reflect.luna;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("unused")
public class Level0 {

    private int intValue;
    private Double doubleValue;
    private Instant instantValue;
    private String stringValue;

    public Level0() {
    }

    public Level0(final Level0 origin) {
        this.intValue = origin.intValue;
        this.doubleValue = origin.doubleValue;
        this.instantValue = origin.instantValue;
        this.stringValue = origin.stringValue;
    }

    public final int getIntValue() {
        return intValue;
    }

    public final Level0 setIntValue(final int intValue) {
        this.intValue = intValue;
        return this;
    }

    public final Double getDoubleValue() {
        return doubleValue;
    }

    public final Level0 setDoubleValue(final Double doubleValue) {
        this.doubleValue = doubleValue;
        return this;
    }

    public final Instant getInstantValue() {
        return instantValue;
    }

    public final Level0 setInstantValue(final Instant instantValue) {
        this.instantValue = instantValue;
        return this;
    }

    public final String getStringValue() {
        return stringValue;
    }

    public final Level0 setStringValue(final String stringValue) {
        this.stringValue = stringValue;
        return this;
    }

    @SuppressWarnings("DesignForExtension")
    protected Stream<Object> stream() {
        return Stream.of(intValue, doubleValue, instantValue, stringValue);
    }

    public final List<Object> toList() {
        return stream().collect(Collectors.toList());
    }

    @Override
    public final boolean equals(final Object obj) {
        return (this == obj) || ((obj instanceof Level0) && toList().equals(((Level0) obj).toList()));
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
