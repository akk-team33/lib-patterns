package de.team33.sample.patterns.building.elara;

import de.team33.patterns.random.tarvos.Generator;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.time.Instant;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class BuildableTest implements Generator {

    private final Random random = new Random();

    @Test
    final void getIntValue() {
        final Buildable expected = new Buildable(nextInt(),
                                                 nextDouble(),
                                                 Instant.now().plusSeconds(nextLong(-1000L, 1000L)),
                                                 nextString(nextInt(24), "abc"));
        final Buildable result = new Buildable(expected);
        assertEquals(expected, result);
    }

    @Override
    public final BigInteger nextBits(final int numBits) {
        return new BigInteger(numBits, random);
    }
}