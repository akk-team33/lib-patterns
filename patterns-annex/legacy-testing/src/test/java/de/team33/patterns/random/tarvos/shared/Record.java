package de.team33.patterns.random.tarvos.shared;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Collections.unmodifiableList;

@SuppressWarnings({"unused", "BooleanParameter", "ConstructorWithTooManyParameters"})
public class Record {

    private final boolean booleanValue;
    private final String stringValue;
    private final int intValue;
    private final Long longValue;
    private final List<String> stringList;
    private final List<Long> longList;

    public Record(final boolean booleanValue, final String stringValue, final int intValue, final Long longValue,
                  final List<String> stringList, final List<Long> longList) {
        this.booleanValue = booleanValue;
        this.stringValue = stringValue;
        this.intValue = intValue;
        this.longValue = longValue;
        this.stringList = (null == stringList) ? null : unmodifiableList(new ArrayList<>(stringList));
        this.longList = (null == longList) ? null : unmodifiableList(new ArrayList<>(longList));
    }

    public Record(final boolean booleanValue, final String stringValue, final int intValue, final Long longValue) {
        this(booleanValue, stringValue, intValue, longValue, null, null);
    }

    public Record() {
        this(false, null, 0, null, null, null);
    }

    public final boolean booleanValue() {
        return booleanValue;
    }

    public final String stringValue() {
        return stringValue;
    }

    public final int intValue() {
        return intValue;
    }

    public final Long longValue() {
        return longValue;
    }

    public final List<String> stringList() {
        return stringList;
    }

    public final List<Long> longList() {
        return longList;
    }

    @Override
    public final boolean equals(final Object obj) {
        return (this == obj) || ((obj instanceof Record) && toList().equals(((Record) obj).toList()));
    }

    @Override
    public final int hashCode() {
        return toList().hashCode();
    }

    @Override
    public final String toString() {
        return toList().toString();
    }

    public final List<Object> toList() {
        return Arrays.asList(booleanValue, stringValue, intValue, longValue, stringList, longList);
    }
}
