package de.team33.samples.patterns.features.rhea;

import de.team33.patterns.features.rhea.Feature;
import de.team33.patterns.features.rhea.FeatureHub;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

public class InternalFeatures {

    private static final Feature.Key<InternalFeatures, List<Object>> LIST_VALUE =
            i -> Arrays.asList(i.intValue, i.stringValue, i.instantValue);
    private static final Feature.Key<InternalFeatures, String> STRING_VALUE =
            i -> i.toList().toString();
    private static final Feature.Key<InternalFeatures, Integer> HASH_VALUE =
            i -> i.toList().hashCode();

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
        return features.get(LIST_VALUE);
    }

    @Override
    public boolean equals(Object obj) {
        return (this == obj)
               || ((obj instanceof InternalFeatures) && toList().equals(((InternalFeatures) obj).toList()));
    }

    @Override
    public int hashCode() {
        return features.get(HASH_VALUE);
    }

    @Override
    public String toString() {
        return features.get(STRING_VALUE);
    }
}
