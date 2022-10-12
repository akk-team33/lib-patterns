package de.team33.test.patterns.random.shared;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Buildable {

    private String stringValue;
    private int intValue;
    private Long longValue;
    private List<String> stringList;
    private List<Long> longList;

    private Buildable(final Builder builder) {
        stringValue = builder.stringValue;
        intValue = builder.intValue;
        longValue = builder.longValue;
        stringList = new ArrayList<>(builder.stringList);
        longList = new ArrayList<>(builder.longList);
    }

    public static Builder builder() {
        return new Builder();
    }

    @SuppressWarnings("rawtypes")
    private static List asList(final Buildable sample) {
        return Arrays.asList(
                sample.stringValue,
                sample.intValue,
                sample.longValue,
                sample.stringList,
                sample.longList);
    }

    public final String getStringValue() {
        return stringValue;
    }

    public final Buildable setStringValue(final String stringValue) {
        this.stringValue = stringValue;
        return this;
    }

    @Override
    public final boolean equals(final Object o) {
        return (this == o) || ((o instanceof Buildable) && asList(this).equals(asList((Buildable) o)));
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

    public final Buildable setIntValue(final int intValue) {
        this.intValue = intValue;
        return this;
    }

    public final Long getLongValue() {
        return longValue;
    }

    public final Buildable setLongValue(final Long longValue) {
        this.longValue = longValue;
        return this;
    }

    public final List<String> getStringList() {
        return Collections.unmodifiableList(stringList);
    }

    public final Buildable setStringList(final List<String> stringList) {
        this.stringList = new ArrayList<>(stringList);
        return this;
    }

    public final List<Long> getLongList() {
        return Collections.unmodifiableList(longList);
    }

    public final Buildable setLongList(final List<Long> longList) {
        this.longList = new ArrayList<>(longList);
        return this;
    }

    @SuppressWarnings("FieldHasSetterButNoGetter")
    public static class Builder {

        private String stringValue;
        private int intValue;
        private Long longValue;
        private List<String> stringList = Collections.emptyList();
        private List<Long> longList = Collections.emptyList();

        public final Builder setStringValue(final String stringValue) {
            this.stringValue = stringValue;
            return this;
        }

        public final Builder setIntValue(final int intValue) {
            this.intValue = intValue;
            return this;
        }

        public final Builder setLongValue(final Long longValue) {
            this.longValue = longValue;
            return this;
        }

        public final Builder setStringList(final List<String> stringList) {
            this.stringList = new ArrayList<>(stringList);
            return this;
        }

        public final Builder setLongList(final List<Long> longList) {
            this.longList = new ArrayList<>(longList);
            return this;
        }

        public final Buildable build() {
            return new Buildable(this);
        }
    }
}
