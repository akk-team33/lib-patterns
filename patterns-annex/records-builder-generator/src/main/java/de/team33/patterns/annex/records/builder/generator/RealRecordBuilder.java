package de.team33.patterns.annex.records.builder.generator;

import java.time.Instant;

public class RealRecordBuilder {

    private int intValue;
    private Long longValue;
    private String stringValue;
    private Instant instantValue;

    public final RealRecordBuilder intValue(final int intValue) {
        this.intValue = intValue;
        return this;
    }

    public final RealRecordBuilder longValue(final Long longValue) {
        this.longValue = longValue;
        return this;
    }

    public final RealRecordBuilder stringValue(final String stringValue) {
        this.stringValue = stringValue;
        return this;
    }

    public final RealRecordBuilder instantValue(final Instant instantValue) {
        this.instantValue = instantValue;
        return this;
    }

    public final RealRecord build() {
        return new RealRecord(intValue, longValue, stringValue, instantValue);
    }
}
