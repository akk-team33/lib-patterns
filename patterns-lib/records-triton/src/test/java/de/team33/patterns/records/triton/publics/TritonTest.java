package de.team33.patterns.records.triton.publics;

import de.team33.patterns.records.triton.Triton;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TritonTest {

    private static Sample anySample() {
        return new Sample(UUID.randomUUID().toString(),
                          UUID.randomUUID().toString(),
                          UUID.randomUUID().toString());
    }

    @Test
    void jsonRoundTrip() {
        final Sample origin = anySample();
        final String stage = Triton.toJson(origin);
        final Sample result = Triton.toRecord(Sample.class, stage);
        assertEquals(origin, result);
    }

    @Test
    void mapRoundTrip() {
        final Sample origin = anySample();
        final Map<String, Object> stage = Triton.toMap(origin);
        final Sample result = Triton.toRecord(Sample.class, stage);
        assertEquals(origin, result);
    }

    private record Sample(String name, String create, String update) {
    }
}