package de.team33.sample.patterns.reflect.luna;

import de.team33.patterns.reflect.luna.Fields;
import de.team33.patterns.reflect.luna.Properties;

import java.time.Instant;
import java.util.stream.Stream;

public class Level2 extends Level1 {

    private static final Properties<Level2> PROPS = Fields.properties(Level2.class,
                                                                      (source, field) -> field.get(source),
                                                                      (target, field, value) -> field.set(target, value));

    private int intValue2;
    private Double doubleValue2;
    private Instant instantValue2;
    private String stringValue2;

    public Level2() {
    }

    public Level2(final Level0 origin) {
        super(origin);
        if (origin instanceof Level2) {
            PROPS.copy((Level2) origin, this);
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
        return Stream.concat(super.stream(), PROPS.stream(this));
    }
}
