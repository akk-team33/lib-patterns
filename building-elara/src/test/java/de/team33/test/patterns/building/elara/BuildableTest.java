package de.team33.test.patterns.building.elara;

import de.team33.patterns.random.tarvos.Generator;
import de.team33.sample.patterns.building.elara.Buildable;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.time.Instant;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class BuildableTest implements Generator {

    private final Random random = new Random();

    @Test
    final void copyBuildable() {
        final Buildable expected = Buildable.builder()
                                            .setIntValue(nextInt())
                                            .setDoubleValue(nextDouble())
                                            .setInstantValue(Instant.now().plusSeconds(nextLong(-100000L, 100000L)))
                                            .setStringValue(nextString(nextInt(24), "abc"))
                                            .build();
        final Buildable result = expected.toBuilder()
                                         .build();
        assertNotSame(expected, result);
        assertEquals(expected, result);
    }

    @Override
    public final BigInteger nextBits(final int numBits) {
        return new BigInteger(numBits, random);
    }
}
