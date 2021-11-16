package de.team33.test.patterns.production.shared;

import java.util.Map;

public class UnmappableC {

    public UnmappableC() {
    }

    public UnmappableC(final Map<String, Object> origin) {
    }

    public final String toMap() {
        return "ätsch!";
    }
}
