package de.team33.sample.patterns.reflect.luna;

import de.team33.patterns.reflect.luna.Fields;

import java.time.Instant;
import java.util.stream.Stream;

public class Level1 extends Level0 {

    private static final Fields FIELDS = Fields.of(Level1.class);

    private int intValue1;
    private Double doubleValue1;
    private Instant instantValue1;
    private String stringValue1;

    public Level1() {
    }

    public Level1(final Level0 source) {
        super(source);
        if (source instanceof Level1) {
            FIELDS.forEach(field -> field.set(this, field.get(source)));
        }
    }

    public final int getIntValue1() {
        return intValue1;
    }

    public final Level1 setIntValue1(final int intValue1) {
        this.intValue1 = intValue1;
        return this;
    }

    public final Double getDoubleValue1() {
        return doubleValue1;
    }

    public final Level1 setDoubleValue1(final Double doubleValue1) {
        this.doubleValue1 = doubleValue1;
        return this;
    }

    public final Instant getInstantValue1() {
        return instantValue1;
    }

    public final Level1 setInstantValue1(final Instant instantValue1) {
        this.instantValue1 = instantValue1;
        return this;
    }

    public final String getStringValue1() {
        return stringValue1;
    }

    public final Level1 setStringValue1(final String stringValue1) {
        this.stringValue1 = stringValue1;
        return this;
    }

    @SuppressWarnings("DesignForExtension")
    @Override
    protected Stream<Object> stream() {
        return Stream.concat(super.stream(), FIELDS.map(field -> field.get(this)));
    }
}
