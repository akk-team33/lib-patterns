package de.team33.patterns.annex.records.builder.generator;

import de.team33.patterns.lazy.narvi.Lazy;

import java.time.Instant;
import java.util.List;

public final class PseudoRecord {

    private final int intValue;
    private final Long longValue;
    private final String stringValue;
    private final Instant instantValue;
    private transient final Lazy<List<Object>> asList;

    public PseudoRecord(int intValue, Long longValue, String stringValue, Instant instantValue) {
        this.intValue = intValue;
        this.longValue = longValue;
        this.stringValue = stringValue;
        this.instantValue = instantValue;
        this.asList = Lazy.init(() -> List.of(this.intValue, this.longValue, this.stringValue, this.instantValue));
    }

    public PseudoRecord() {
        this(0, null, null, null);
    }

    public int intValue() {
        return intValue;
    }

    public Long longValue() {
        return longValue;
    }

    public String stringValue() {
        return stringValue;
    }

    public Instant instantValue() {
        return instantValue;
    }

    private List<Object> asList() {
        return asList.get();
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj | (obj instanceof PseudoRecord other && asList().equals(other.asList()));
    }

    @Override
    public int hashCode() {
        return asList().hashCode();
    }

    @Override
    public String toString() {
        return asList().toString();
    }
}
