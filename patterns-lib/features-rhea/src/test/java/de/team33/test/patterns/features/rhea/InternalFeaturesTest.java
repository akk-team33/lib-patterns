package de.team33.test.patterns.features.rhea;

import de.team33.patterns.random.tarvos.Generator;
import de.team33.samples.patterns.features.rhea.InternalFeatures;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.time.Instant;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class InternalFeaturesTest {

    private static final Random RANDOM = new Random();
    private static final Generator SUPPLY = numBits -> new BigInteger(numBits, RANDOM);
    private static final String CHARS = "0123456789abcdef";

    private final InternalFeatures original = new InternalFeatures(SUPPLY.nextInt(),
                                                                   SUPPLY.nextString(7, CHARS),
                                                                   Instant.now().minusMillis(SUPPLY.nextShort()));
    private final InternalFeatures other = new InternalFeatures(original.getIntValue(),
                                                                original.getStringValue(),
                                                                original.getInstantValue());
    private final InternalFeatures divergent = new InternalFeatures(original.getIntValue() + SUPPLY.nextInt(),
                                                                    SUPPLY.nextString(5, CHARS),
                                                                    original.getInstantValue().plusMillis(5));

    @Test
    void testEquals() {
        assertEquals(original, other);
        assertNotEquals(original, divergent);
    }

    @Test
    void testHashCode() {
        assertEquals(original.hashCode(), other.hashCode());
        assertNotEquals(original.hashCode(), divergent.hashCode());
    }

    @Test
    void testToString() {
        assertEquals(original.toString(), other.toString());
        assertNotEquals(original.toString(), divergent.toString());
    }
}
