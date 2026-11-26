package de.team33.patterns.lazy.janus.sample;

import de.team33.patterns.lazy.janus.Features;

import java.time.Instant;
import java.util.List;

public class Sample {

    // Features managed for this instance ...
    // --------------------------------------
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

        // when a "normal" property is modified some features must expire ...
        // ------------------------------------------------------------------
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

    // A private List representation of this instance ...
    // --------------------------------------------------
    private final List<Object> toList() {
        return features.get(Key.TO_LIST, () -> List.of(intValue, stringValue, instantValue));
    }

    @Override
    public final boolean equals(final Object obj) {
        return (this == obj) || ((obj instanceof final Sample other) && toList().equals(other.toList()));
    }

    // Also provided as a feature ...
    // ------------------------------
    @Override
    public final int hashCode() {
        return features.get(Key.HASH_CODE, () -> toList().hashCode());
    }

    // Also provided as a feature ...
    // ------------------------------
    @Override
    public final String toString() {
        return features.get(Key.TO_STRING, () -> toList().toString());
    }

    // Local keys for the features provided by this class ...
    // ------------------------------------------------------
    private interface Key<R> extends Features.Key<R> {

        // ... to simplify the local Key definitions ...
        // ---------------------------------------------
        Key<List<Object>> TO_LIST = named("TO_LIST");
        Key<Integer> HASH_CODE = named("HASH_CODE");
        Key<String> TO_STRING = named("TO_STRING");

        // Convenient factory for named keys ...
        // -------------------------------------
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
