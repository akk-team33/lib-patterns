package de.team33.patterns.shared.serial.charon;

import de.team33.patterns.arbitrary.mimas.Generator;

import java.math.BigInteger;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Supply implements Generator {

    private static final String CHARS = "abcdefghijklmnopqrstuvwxyz-ABCDEFGHIJKLMNOPQRSTUVWXYZ_0123456789";

    private final Random random = new Random();

    @Override
    public BigInteger anyBits(int numBits) {
        return new BigInteger(numBits, random);
    }

    public String nextString() {
        return anyString(anyInt(16), CHARS);
    }

    public List<String> nextList(final int minSize, final int maxSize) {
        return Stream.generate(this::nextString)
                     .limit(anyInt(minSize, maxSize + 1))
                     .collect(Collectors.toList());
    }

    public List<String> nextList(int size) {
        return Stream.generate(this::nextString)
                     .limit(size)
                     .collect(Collectors.toList());
    }

    public List<String> nextChargedList() {
        return nextList(anyInt(1, 4));
    }
}
