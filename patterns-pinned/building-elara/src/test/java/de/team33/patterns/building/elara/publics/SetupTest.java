package de.team33.patterns.building.elara.publics;

import de.team33.patterns.building.elara.sample.Buildable;
import de.team33.patterns.building.elara.sample.Supply;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SetupTest {

    private static final Supply SUPPLY = new Supply();

    @Test
    final void plain() {
        final Buildable expected = SUPPLY.anyBuildable();
        final Buildable result = Buildable.builder()
                                          .setIntValue(expected.getIntValue())
                                          .setDoubleValue(expected.getDoubleValue())
                                          .setInstantValue(expected.getInstantValue())
                                          .setStringValue(expected.getStringValue())
                                          .build();
        assertEquals(expected, result);
    }
}