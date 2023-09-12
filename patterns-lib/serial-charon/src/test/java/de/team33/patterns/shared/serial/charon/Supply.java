package de.team33.patterns.shared.serial.charon;

import de.team33.patterns.random.tarvos.Generator;

import java.math.BigInteger;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Supply implements Generator {

    private static final String CHARS = "abcdefghijklmnopqrstuvwxyz-ABCDEFGHIJKLMNOPQRSTUVWXYZ_0123456789";

    private final Random random = new Random();

    @Override
    public BigInteger nextBits(int numBits) {
        return new BigInteger(numBits, random);
    }

    public String nextString() {
        return nextString(nextInt(16), CHARS);
    }

    public List<String> nextList(final int minSize, final int maxSize) {
        return Stream.generate(this::nextString)
                     .limit(nextInt(minSize, maxSize + 1))
                     .collect(Collectors.toList());
    }

    public List<String> nextList(int size) {
        return Stream.generate(this::nextString)
                     .limit(size)
                     .collect(Collectors.toList());
    }

    public List<String> nextChargedList() {
        return nextList(nextInt(1, 4));
    }
}
