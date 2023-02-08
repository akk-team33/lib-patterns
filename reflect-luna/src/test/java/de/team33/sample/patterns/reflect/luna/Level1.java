package de.team33.sample.patterns.reflect.luna;

import de.team33.patterns.reflect.luna.Fields;
import de.team33.patterns.reflect.luna.Properties;

import java.lang.reflect.Field;
import java.time.Instant;
import java.util.stream.Stream;

public class Level1 extends Level0 {

    private static final Properties<Level1> PROPS = Fields.properties(Level1.class, Level1::get1, Level1::set1);

    private int intValue1;
    private Double doubleValue1;
    private Instant instantValue1;
    private String stringValue1;

    public Level1() {
    }

    public Level1(final Level0 origin) {
        super(origin);
        if (origin instanceof Level1) {
            PROPS.copy((Level1) origin, this);
        }
    }

    private Object get1(final Field field) throws IllegalAccessException {
        return field.get(this);
    }

    private void set1(final Field field, final Object value) throws IllegalAccessException {
        field.set(this, value);
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
        return Stream.concat(super.stream(), PROPS.stream(this));
    }
}
