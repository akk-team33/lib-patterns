package de.team33.patterns.arbitrary.mimas.sample;

import de.team33.patterns.arbitrary.mimas.Generator;
import de.team33.patterns.records.metis.Metis;

import java.util.Map;

public class Producer extends Generator.Basic {

    public Person anyPerson() {
        return Metis.toRecord(Person.class, anyMap(Metis.description(Person.class)));
    }

    private <K> Map<K, Object> anyMap(final Map<K, Class<?>> description) {
        return null;
    }
}
