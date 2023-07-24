package de.team33.test.patterns.collection.ceres;

import de.team33.patterns.random.tarvos.Generator;

import java.math.BigInteger;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Supply extends Random implements Generator {

    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyz-ABCDEFGHIJKLMNOPQRSTUVWXYZ_0123456789";

    @Override
    public BigInteger nextBits(final int numBits) {
        return new BigInteger(numBits, this);
    }

    public int nextLength() {
        return nextInt(24);
    }

    public String nextString() {
        return nextString(nextLength(), CHARACTERS);
    }

    public List<String> nextStringList(final int size) {
        return Stream.generate(this::nextString)
                     .limit(size)
                     .collect(Collectors.toList());
    }

    public Set<String> nextStringSet(final int size) {
        return Stream.generate(this::nextString)
                     .distinct()
                     .limit(size)
                     .collect(Collectors.toSet());
    }

    public String nextStringExcluding(final Collection<?> obsolete) {
        final String result = nextString();
        return obsolete.contains(result) ? nextStringExcluding(obsolete) : result;
    }
}
