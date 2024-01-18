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
    private static final String CHARS = "\"\t abcdefghijklmnopqrstuvwxyz-ABCDEFGHIJKLMNOPQRSTUVWXYZ_0123456789";

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
        return nextNormal(3);
    }

    public final Normal nextNormal(final int maxDepth) {
        final int selection = nextInt(6);
        if ((1 > maxDepth) || (2 > selection))
            return Normal.of(nextString());
        else if (3 > selection)
            return Normal.of(nextNormalSet(maxDepth - 1));
        else if (4 > selection)
            return Normal.of(nextNormalList(maxDepth - 1));
        else
            return Normal.of(nextNormalMap(0, maxDepth - 1));

    }

    public final Set<Normal> nextNormalSet() {
        return nextNormalSet(3);
    }

    public final Set<Normal> nextNormalSet(final int maxDepth) {
        return Stream.generate(() -> nextNormal(maxDepth))
                     .limit(nextLong(1, 16))
                     .collect(Collectors.toSet());
    }

    public final List<Normal> nextNormalList() {
        return nextNormalList(3);
    }

    public final List<Normal> nextNormalList(final int maxDepth) {
        return Stream.generate(() -> nextNormal(maxDepth))
                     .limit(nextLong(1, 16))
                     .collect(Collectors.toList());
    }

    public final Map<Normal, Normal> nextNormalMap() {
        return nextNormalMap(0, 3);
    }

    public final Map<Normal, Normal> nextNormalMap(final int keyDepth, final int valDepth) {
        return Stream.generate(() -> nextNormal(valDepth))
                     .limit(nextLong(1, 16))
                     .collect(Collectors.toMap(value -> nextNormal(keyDepth), Function.identity(), (l, r) -> r));
    }

    public String[] nextStringArray() {
        return Stream.generate(this::nextString)
                     .limit(nextInt(1, 8))
                     .toArray(String[]::new);
    }
}
