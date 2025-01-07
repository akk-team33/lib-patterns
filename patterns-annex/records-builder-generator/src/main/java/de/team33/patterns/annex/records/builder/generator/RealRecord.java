package de.team33.patterns.annex.records.builder.generator;

import java.time.Instant;

public record RealRecord(int intValue, Long longValue, String stringValue, Instant instantValue) {

    public RealRecord() {
        this(0, null, null, null);
    }
}
