package de.team33.patterns.records.rho.sample;

import java.time.Instant;
import java.util.List;

@SuppressWarnings({"unused", "AssignmentOrReturnOfFieldWithMutableType", "WeakerAccess"})
public record Extended(String name, String update, Element[] elements) {

    public Instant updateTime() {
        return Instant.parse(update);
    }

    public List<Element> elementList() {
        return List.of(elements);
    }

    public record Element(String name, Object value) {
    }
}
