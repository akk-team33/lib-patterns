package de.team33.patterns.random.tarvos.shared;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Sample {

    private boolean booleanValue;
    private String stringValue;
    private int intValue;
    private Long longValue;
    private List<String> stringList = Collections.emptyList();
    private List<Long> longList = Collections.emptyList();

    public static void setNothing(final Object nothing) {
        throw new UnsupportedOperationException("should not be called anyways");
    }

    @SuppressWarnings("rawtypes")
    private static List asList(final Sample sample) {
        return Arrays.asList(
                sample.booleanValue,
                sample.stringValue,
                sample.intValue,
                sample.longValue,
                sample.stringList,
                sample.longList);
    }

    public final String getStringValue() {
        return stringValue;
    }

    public final Sample setStringValue(final String stringValue) {
        this.stringValue = stringValue;
        return this;
    }

    @Override
    public final boolean equals(final Object o) {
        return (this == o) || ((o instanceof Sample) && asList(this).equals(asList((Sample) o)));
    }

    @Override
    public final int hashCode() {
        return asList(this).hashCode();
    }

    @Override
    public final String toString() {
        return asList(this).toString();
    }

    public final int getIntValue() {
        return intValue;
    }

    public final Sample setIntValue(final int intValue) {
        this.intValue = intValue;
        return this;
    }

    public final Long getLongValue() {
        return longValue;
    }

    public final Sample setLongValue(final Long longValue) {
        this.longValue = longValue;
        return this;
    }

    public final List<String> getStringList() {
        return Collections.unmodifiableList(stringList);
    }

    public final Sample setStringList(final List<String> stringList) {
        this.stringList = new ArrayList<>(stringList);
        return this;
    }

    public final List<Long> getLongList() {
        return Collections.unmodifiableList(longList);
    }

    public final Sample setLongList(final List<Long> longList) {
        this.longList = new ArrayList<>(longList);
        return this;
    }

    public final boolean isBooleanValue() {
        return booleanValue;
    }

    public final Sample setBooleanValue(final boolean booleanValue) {
        this.booleanValue = booleanValue;
        return this;
    }
}
