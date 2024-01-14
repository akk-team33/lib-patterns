package de.team33.patterns.normal.iocaste.testing;

import de.team33.patterns.normal.iocaste.Normal;
import de.team33.patterns.random.tarvos.Generator;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Supply implements Generator {

    private static final Random RANDOM = new SecureRandom();
    private static final String CHARS = "abcdefghijklmnopqrstuvwxyz-ABCDEFGHIJKLMNOPQRSTUVWXYZ_0123456789";

    @Override
    public final BigInteger nextBits(final int numBits) {
        return new BigInteger(numBits, RANDOM);
    }

    public final char nextChar() {
        return nextChar(CHARS);
    }

    public final String nextString() {
        return nextString(nextInt(25), CHARS);
    }

    public final Normal nextNormal() {
        final int sel = nextInt(12);
        if (sel < 10) {
            return Normal.of(nextString());
        } else if (sel < 11) {
            return Normal.of(nextNormalSet());
        } else {
            return Normal.of(nextNormalList());
        }
    }

    public final Set<Normal> nextNormalSet() {
        return Stream.generate(this::nextNormal)
                     .limit(nextLong(1, 16))
                     .collect(Collectors.toSet());
    }

    public final List<Normal> nextNormalList() {
        return Stream.generate(this::nextNormal)
                     .limit(nextLong(1, 16))
                     .collect(Collectors.toList());
    }

    public final Map<Normal, Normal> nextNormalMap() {
        return Stream.generate(this::nextNormal)
                     .limit(nextLong(1, 16))
                     .collect(Collectors.toMap(value -> Normal.of(nextString()), Function.identity()));
    }

    public String[] nextStringArray() {
        return Stream.generate(this::nextString)
                     .limit(nextInt(1, 8))
                     .toArray(String[]::new);
    }
}
