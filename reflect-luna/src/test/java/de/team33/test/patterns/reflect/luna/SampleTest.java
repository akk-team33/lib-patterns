package de.team33.test.patterns.reflect.luna;

import de.team33.patterns.random.tarvos.Generator;
import de.team33.sample.patterns.reflect.luna.Sample;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.time.Instant;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class SampleTest extends Random implements Generator {

    @Test
    final void copy() {
        final Sample expected = new Sample.Builder().setIntValue(nextInt())
                                                    .setDoubleValue(nextDouble())
                                                    .setInstantValue(
                                                            Instant.now()
                                                                   .plusSeconds(nextLong(-10000, 10000)))
                                                    .setStringValue(
                                                            nextString(nextInt(0, 24), "abc"))
                                                    .build();
        final Sample result = new Sample(expected);
        assertEquals(expected, result);
    }

    @Override
    public BigInteger nextBits(int numBits) {
        return new BigInteger(numBits, this);
    }
}
