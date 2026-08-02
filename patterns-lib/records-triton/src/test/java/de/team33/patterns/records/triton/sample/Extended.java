package de.team33.patterns.records.triton.sample;

import java.time.Instant;
import java.util.List;

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
