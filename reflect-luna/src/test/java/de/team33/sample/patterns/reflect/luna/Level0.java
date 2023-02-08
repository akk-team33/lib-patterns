package de.team33.sample.patterns.reflect.luna;

import de.team33.patterns.reflect.luna.Fields;
import de.team33.patterns.reflect.luna.Properties;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("unused")
public class Level0 {

    private static final Fields.Getter<Level0> GETTER = (source, field) -> field.get(source);
    private static final Fields.Setter<Level0> SETTER = (target, field, value) -> field.set(target, value);
    private static final Properties<Level0> PROPS = Fields.properties(Level0.class, GETTER, SETTER);

    private int intValue;
    private Double doubleValue;
    private Instant instantValue;
    private String stringValue;

    public Level0() {
    }

    public Level0(final Level0 origin) {
        PROPS.copy(origin, this);
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
        return PROPS.stream(this);
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
