package de.team33.sample.patterns.reflect.luna;

import de.team33.patterns.reflect.luna.Fields;

import java.time.Instant;
import java.util.stream.Stream;

public class Level2 extends Level1 {

    private static final Fields FIELDS = Fields.of(Level2.class);

    private int intValue2;
    private Double doubleValue2;
    private Instant instantValue2;
    private String stringValue2;

    public Level2() {
    }

    public Level2(final Level0 source) {
        super(source);
        if (source instanceof Level2) {
            FIELDS.forEach(field -> field.set(this, field.get(source)));
        }
    }

    public final int getIntValue2() {
        return intValue2;
    }

    public final Level2 setIntValue2(final int intValue2) {
        this.intValue2 = intValue2;
        return this;
    }

    public final Double getDoubleValue2() {
        return doubleValue2;
    }

    public final Level2 setDoubleValue2(final Double doubleValue2) {
        this.doubleValue2 = doubleValue2;
        return this;
    }

    public final Instant getInstantValue2() {
        return instantValue2;
    }

    public final Level2 setInstantValue2(final Instant instantValue2) {
        this.instantValue2 = instantValue2;
        return this;
    }

    public final String getStringValue2() {
        return stringValue2;
    }

    public final Level2 setStringValue2(final String stringValue2) {
        this.stringValue2 = stringValue2;
        return this;
    }

    @SuppressWarnings("DesignForExtension")
    @Override
    protected Stream<Object> stream() {
        return Stream.concat(super.stream(), FIELDS.map(field -> field.get(this)));
    }
}
