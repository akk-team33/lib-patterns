package de.team33.patterns.building.elara.sample;

import de.team33.patterns.arbitrary.mimas.Charger;
import de.team33.patterns.arbitrary.mimas.Generator;

import java.math.BigInteger;
import java.time.Instant;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Supply extends Random implements Generator, Charger {

    public static final long SECONDS_PER_YEAR = 31536000L;
    public static final String LOWER_LETTERS = "abcdefghijklmnopqrstuvwxyz";
    public static final String UPPER_LETTERS = LOWER_LETTERS.toUpperCase(Locale.ROOT);
    public static final String LETTERS = LOWER_LETTERS + UPPER_LETTERS;

    @Override
    public final BigInteger anyBits(final int numBits) {
        return new BigInteger(numBits, this);
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
