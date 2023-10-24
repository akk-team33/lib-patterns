package de.team33.samples.patterns.features.rhea;

import de.team33.patterns.features.rhea.Feature;
import de.team33.patterns.features.rhea.FeatureHub;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

public class InternalFeatures {

    private final int intValue;
    private final String stringValue;
    private final Instant instantValue;
    private final transient Feature.Hub<InternalFeatures> features;

    public InternalFeatures(final int intValue, final String stringValue, final Instant instantValue) {
        this.intValue = intValue;
        this.stringValue = stringValue;
        this.instantValue = instantValue;
        this.features = new Feature.Holder<InternalFeatures>() {
            @Override
            protected InternalFeatures host() {
                return InternalFeatures.this;
            }
        };
    }

    public int getIntValue() {
        return intValue;
    }

    public String getStringValue() {
        return stringValue;
    }

    public Instant getInstantValue() {
        return instantValue;
    }

    private List<Object> toList() {
        return features.get(Key.LIST_VALUE);
    }

    @Override
    public boolean equals(Object obj) {
        return (this == obj)
               || ((obj instanceof InternalFeatures) && toList().equals(((InternalFeatures) obj).toList()));
    }

    @Override
    public int hashCode() {
        return features.get(Key.HASH_VALUE);
    }

    @Override
    public String toString() {
        return features.get(Key.STRING_VALUE);
    }

    private interface Key<R> extends Feature.Key<InternalFeatures, R> {

        Key<List<Object>> LIST_VALUE = i -> Arrays.asList(i.intValue, i.stringValue, i.instantValue);
        Key<String> STRING_VALUE = i -> i.toList().toString();
        Key<Integer> HASH_VALUE = i -> i.toList().hashCode();
    }
}
