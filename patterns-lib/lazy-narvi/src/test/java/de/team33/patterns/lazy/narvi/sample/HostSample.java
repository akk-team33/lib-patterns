package de.team33.patterns.lazy.narvi.sample;

import de.team33.patterns.lazy.narvi.LazyFeatures;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

public class HostSample {

    private final transient HostFeatures features = new HostFeatures();
    private int intValue;
    private String stringValue;
    private Instant instantValue;

    public final int getIntValue() {
        return intValue;
    }

    public final HostSample setIntValue(final int intValue) {
        return features.reset(() -> this.intValue = intValue);
    }

    public final String getStringValue() {
        return stringValue;
    }

    public final HostSample setStringValue(final String stringValue) {
        return features.reset(() -> this.stringValue = stringValue);
    }

    public final Instant getInstantValue() {
        return instantValue;
    }

    public final HostSample setInstantValue(final Instant instantValue) {
        return features.reset(() -> this.instantValue = instantValue);
    }

    public final List<Object> toList() {
        return features.get(Key.LIST);
    }

    @Override
    public final boolean equals(final Object obj) {
        return (this == obj) || ((obj instanceof final HostSample other) && toList().equals(other.toList()));
    }

    @Override
    public final int hashCode() {
        return features.get(Key.HASH);
    }

    @Override
    public final String toString() {
        return features.get(Key.STRING);
    }

    @FunctionalInterface
    private interface Key<R> extends LazyFeatures.Key<HostFeatures, R> {

        Key<List<Object>> LIST = HostFeatures::newList;
        Key<Integer> HASH = HostFeatures::newHash;
        Key<String> STRING = HostFeatures::newString;
    }

    private class HostFeatures extends LazyFeatures<HostFeatures> {

        @Override
        protected final HostFeatures host() {
            return this;
        }

        private List<Object> newList() {
            return Arrays.asList(intValue, stringValue, instantValue);
        }

        private Integer newHash() {
            return toList().hashCode();
        }

        private String newString() {
            return HostSample.class.getSimpleName() + toList().toString();
        }

        private HostSample reset(final Runnable runnable) {
            reset();
            runnable.run();
            return HostSample.this;
        }
    }
}
