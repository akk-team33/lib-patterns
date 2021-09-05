package de.team33.test.patterns.properties.e1;

import de.team33.patterns.properties.e1.Properties;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class PropertiesTest {

    @Test
    void add() {
        Properties<Instant> sample = Properties.add("epochSecond", Instant::getEpochSecond)
                                               .add("nano", Instant::getNano)
                                               .build();
        assertTrue(sample.equals(Instant.ofEpochSecond(0L, 0L), Instant.ofEpochSecond(0L, 0L)));
    }
}