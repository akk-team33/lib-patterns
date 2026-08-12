package de.team33.patterns.arbitrary.mimas.sample;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Buildable {

    private final String stringValue;
    private final int intValue;
    private final Long longValue;
    private final List<String> stringList;
    private final List<Long> longList;

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
        return List.of(
                sample.stringValue,
                sample.intValue,
                sample.longValue,
                sample.stringList,
                sample.longList);
    }

    public final String getStringValue() {
        return stringValue;
    }

    @Override
    public final boolean equals(final Object obj) {
        return (this == obj) || ((obj instanceof final Buildable other) && asList(this).equals(asList(other)));
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

    public final Long getLongValue() {
        return longValue;
    }

    public final List<String> getStringList() {
        return Collections.unmodifiableList(stringList);
    }

    public final List<Long> getLongList() {
        return Collections.unmodifiableList(longList);
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
