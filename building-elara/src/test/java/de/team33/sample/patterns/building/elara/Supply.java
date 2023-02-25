package de.team33.sample.patterns.building.elara;

import de.team33.patterns.random.tarvos.Charger;
import de.team33.patterns.random.tarvos.Generator;

import java.math.BigInteger;
import java.time.Instant;
import java.util.Locale;
import java.util.Random;

public class Supply extends Random implements Generator, Charger {

    public static final long SECONDS_PER_YEAR = 31536000L;
    public static final String LOWER_LETTERS = "abcdefghijklmnopqrstuvwxyz";
    public static final String UPPER_LETTERS = LOWER_LETTERS.toUpperCase(Locale.ROOT);
    public static final String LETTERS = LOWER_LETTERS + UPPER_LETTERS;

    @Override
    public final BigInteger nextBits(final int numBits) {
        return new BigInteger(numBits, this);
    }

    public final Buildable nextBuildable() {
        return charge(Buildable.builder()).build();
    }

    @SuppressWarnings("unused")
    public final Instant nextInstant() {
        return Instant.now().minusSeconds(nextLong(SECONDS_PER_YEAR));
    }

    public final String nextString() {
        return nextString(nextInt(1, 24), LETTERS);
    }
}
