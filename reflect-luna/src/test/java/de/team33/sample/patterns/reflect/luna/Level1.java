package de.team33.sample.patterns.reflect.luna;

import java.time.Instant;
import java.util.stream.Stream;

public class Level1 extends Level0 {

    private int intValue1;
    private Double doubleValue1;
    private Instant instantValue1;
    private String stringValue1;

    public Level1() {
    }

    public Level1(final Level0 origin) {
        super(origin);
        if (origin instanceof Level1) {
            this.intValue1 = ((Level1)origin).intValue1;
            this.doubleValue1 = ((Level1)origin).doubleValue1;
            this.instantValue1 = ((Level1)origin).instantValue1;
            this.stringValue1 = ((Level1)origin).stringValue1;
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
        return Stream.concat(super.stream(), Stream.of(intValue1, doubleValue1, instantValue1, stringValue1));
    }
}
