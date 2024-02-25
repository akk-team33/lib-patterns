package de.team33.patterns.reflect.pandora.sample;

import de.team33.patterns.reflect.pandora.Getters;
import de.team33.patterns.reflect.pandora.Mapper;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static java.util.Objects.requireNonNull;

public abstract class Sample02 {

    private static final Getters<Sample02> GETTERS = Getters.of(Sample02.class);

    public abstract int getIntValue();

    public abstract Long getLongValue();

    public abstract String getStringValue();

    public abstract Instant getInstantValue();

    public abstract List<Object> getListValue();

    public final Mutable toMutable() {
        return Mutable.MAPPER.map(this, new Mutable());
    }

    private Map<String, Object> toMap() {
        return GETTERS.toMap(this);
    }

    @Override
    public final int hashCode() {
        return toMap().hashCode();
    }

    @Override
    public final boolean equals(final Object obj) {
        return (this == obj) || ((obj instanceof Sample02) && toMap().equals(((Sample02) obj).toMap()));
    }

    @Override
    public final String toString() {
        return toMap().toString();
    }

    public static class Proxy extends Sample02 {

        private final Sample02 backing;

        public Proxy(final Sample02 backing) {
            this.backing = backing;
        }

        @Override
        public final int getIntValue() {
            return backing.getIntValue();
        }

        @Override
        public final Long getLongValue() {
            return backing.getLongValue();
        }

        @Override
        public final String getStringValue() {
            return backing.getStringValue();
        }

        @Override
        public final Instant getInstantValue() {
            return backing.getInstantValue();
        }

        @Override
        public final List<Object> getListValue() {
            return backing.getListValue();
        }
    }

    public static class Mutable extends Sample02 {

        private static final Mapper<Sample02, Mutable> MAPPER = Mapper.mapping(GETTERS, Mutable.class);

        private int intValue;
        private Long longValue = 0L;
        private String stringValue = "";
        private Instant instantValue = Instant.now();
        private List<Object> listValue = Collections.emptyList();

        @Override
        public final int getIntValue() {
            return intValue;
        }

        public final Mutable setIntValue(final int intValue) {
            this.intValue = intValue;
            return this;
        }

        @Override
        public final Long getLongValue() {
            return longValue;
        }

        public final Mutable setLongValue(final Long longValue) {
            this.longValue = requireNonNull(longValue);
            return this;
        }

        @Override
        public final String getStringValue() {
            return stringValue;
        }

        public final Mutable setStringValue(final String stringValue) {
            this.stringValue = requireNonNull(stringValue);
            return this;
        }

        @Override
        public final Instant getInstantValue() {
            return instantValue;
        }

        public final Mutable setInstantValue(final Instant instantValue) {
            this.instantValue = requireNonNull(instantValue);
            return this;
        }

        @Override
        public final List<Object> getListValue() {
            return Collections.unmodifiableList(listValue);
        }

        public final Mutable setListValue(final List<Object> listValue) {
            this.listValue = new ArrayList<>(listValue);
            return this;
        }

        public final Sample02 toImmutable() {
            return new Proxy(toMutable());
        }
    }
}
