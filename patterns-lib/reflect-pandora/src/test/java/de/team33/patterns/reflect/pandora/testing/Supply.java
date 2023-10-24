package de.team33.patterns.reflect.pandora.testing;

import de.team33.patterns.random.tarvos.Charger;
import de.team33.patterns.random.tarvos.Generator;

import java.math.BigInteger;
import java.time.Instant;
import java.util.Random;

public class Supply implements Generator, Charger {

    private final Random random = new Random();

    @Override
    public BigInteger nextBits(int numBits) {
        return new BigInteger(numBits, random);
    }

    public final String nextString() {
        return nextString(nextInt(1, 24), "abcdefghij-ABCDEFGHIJ_0123456789");
    }

    public final Instant nextInstantValue() {
        return Instant.now().plusMillis(nextShort());
    }

    public BeanClass nextBean() {
        return charge(new BeanClass());
    }
}
