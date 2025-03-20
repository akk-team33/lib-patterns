package de.team33.patterns.building.elara.sample;

import de.team33.patterns.arbitrary.mimas.Charger;
import de.team33.patterns.arbitrary.mimas.Generator;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Supply implements Generator, Charger {

    private static final long SECONDS_PER_YEAR = 31536000L;
    private static final String LOWER_LETTERS = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPER_LETTERS = LOWER_LETTERS.toUpperCase(Locale.ROOT);
    private static final String LETTERS = LOWER_LETTERS + UPPER_LETTERS;

    private final Random random = new SecureRandom();

    @Override
    public final BigInteger anyBits(final int numBits) {
        return new BigInteger(numBits, random);
    }

    public final Buildable anyBuildable() {
        return charge(Buildable.builder(), "setup").build();
    }

    @SuppressWarnings("unused")
    public final Instant anyInstant() {
        return Instant.now().minusSeconds(anyLong(SECONDS_PER_YEAR));
    }

    public final String anyString() {
        return anyString(anyInt(1, 24), LETTERS);
    }

    public final List<String> anyStringList(final int size) {
        return Stream.generate(this::anyString).limit(size).collect(Collectors.toList());
    }
}
