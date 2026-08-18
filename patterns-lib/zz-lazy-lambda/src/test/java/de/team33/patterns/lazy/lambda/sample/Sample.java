package de.team33.patterns.lazy.lambda.sample;

import de.team33.patterns.lazy.lambda.Features;

import java.time.Instant;
import java.util.List;

public class Sample {

    // to manage some "lazy" features ...
    // ----------------------------------
    private final transient Features features = new Features();

    // some "normal" properties (with corresponding getters and setters) ...
    // ---------------------------------------------------------------------
    private int intValue;
    private String stringValue;
    private Instant instantValue;

    public final int getIntValue() {
        return intValue;
    }

    public final Sample setIntValue(final int intValue) {
        // when a "normal" property is modified some "lazy" features must expire ...
        // -------------------------------------------------------------------------
        features.reset();

        this.intValue = intValue;
        return this;
    }

    public final String getStringValue() {
        return stringValue;
    }

    public final Sample setStringValue(final String stringValue) {
        features.reset(); // s.a. - features will expire
        this.stringValue = stringValue;
        return this;
    }

    public final Instant getInstantValue() {
        return instantValue;
    }

    public final Sample setInstantValue(final Instant instantValue) {
        features.reset(); // s.a. - features will expire
        this.instantValue = instantValue;
        return this;
    }

    // A private List representation of this instance - a "lazy" feature ...
    // -------------------------------------------------------------
    private final List<Object> toList() {
        return features.get(Key.LIST, () -> List.of(intValue, stringValue, instantValue));
    }

    @Override
    public final boolean equals(final Object obj) {
        return (this == obj) || ((obj instanceof final Sample other) && toList().equals(other.toList()));
    }

    // Also provided as "lazy" feature ...
    // -----------------------------------
    @Override
    public final int hashCode() {
        return features.get(Key.HASH, () -> toList().hashCode());
    }

    // Also provided as "lazy" feature ...
    // -----------------------------------
    @Override
    public final String toString() {
        return features.get(Key.STRING, () -> toList().toString());
    }

    // A local derivative of Features.Key ...
    // --------------------------------------
    private interface Key<R> extends Features.Key<R> {

        // ... to simplify the local Key definitions ...
        // ---------------------------------------------
        Key<List<Object>> LIST = named("LIST");
        Key<Integer> HASH = named("HASH");
        Key<String> STRING = named("STRING");

        static <R> Key<R> named(final String name) {
            return new Key<R>() {
                @Override
                public final String toString() {
                    return name;
                }
            };
        }
    }
}
