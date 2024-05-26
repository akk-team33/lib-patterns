package de.team33.test.patterns.tuple.janus;

import de.team33.patterns.arbitrary.mimas.Generator;

import java.math.BigInteger;
import java.time.Instant;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class TupleTestBase extends Random implements Generator {

    private static final String CHARS = "abcdefghijklmnopqrstuvwxyz ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    @Override
    public final BigInteger anyBits(final int numBits) {
        return new BigInteger(numBits, this);
    }

    final String nextString() {
        return nextString(nextInt(24), CHARS);
    }

    final Instant nextInstant() {
        return Instant.now().plusMillis(nextLong(-1000000, 1000000));
    }

    final <E> List<E> nextList(final Supplier<E> supplier) {
        return Stream.generate(supplier).limit(nextInt(16)).collect(Collectors.toList());
    }

    final <E> Set<E> nextSet(final Supplier<E> supplier) {
        return Stream.generate(supplier).distinct().limit(nextInt(16)).collect(Collectors.toSet());
    }
}
