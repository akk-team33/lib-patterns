package de.team33.test.patterns.building.elara;

import de.team33.sample.patterns.building.elara.Buildable;
import de.team33.sample.patterns.building.elara.Supply;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

class BuildableTest {

    private static final Supply SUPPLY = new Supply();

    @Test
    final void toList() {
        final Buildable sample = Buildable.builder().build();
        assertEquals(4, sample.toList().size());
    }

    @Test
    final void isolation() {
        final Buildable origin = SUPPLY.nextBuildable();
        final Buildable stage = origin.toBuilder()
                                      .build();
        assertNotSame(origin, stage, "<stage> is expected not to be the same instance as <origin>.");
        assertEquals(origin, stage, "<stage> is still expected to be equal to <origin>.");

        final Buildable result = stage.toBuilder()
                                      .setStringValue(SUPPLY.nextString())
                                      .build();
        assertEquals(origin, stage, "<stage> is still expected to be equal to <origin>.");
        assertNotEquals(stage, result, "<result> is not expected to be equal to <stage>.");
    }
}
