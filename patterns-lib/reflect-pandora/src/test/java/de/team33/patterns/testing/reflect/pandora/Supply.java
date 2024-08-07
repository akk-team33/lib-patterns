package de.team33.patterns.testing.reflect.pandora;

import de.team33.patterns.arbitrary.mimas.Charger;
import de.team33.patterns.arbitrary.mimas.Generator;
import de.team33.patterns.sample.reflect.pandora.Sample01;
import de.team33.patterns.sample.reflect.pandora.Sample02;

import java.math.BigInteger;
import java.time.Instant;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Supply implements Generator, Charger {

    private final Random random = new Random();

    @Override
    public BigInteger anyBits(int numBits) {
        return new BigInteger(numBits, random);
    }

    public final String nextString() {
        return anyString(anyInt(1, 24), "abcdefghij-ABCDEFGHIJ_0123456789");
    }

    public final Instant nextInstantValue() {
        return Instant.now().plusMillis(anyShort());
    }

    public final BeanClass nextBean() {
        return charge(new BeanClass());
    }

    public final Sample01 nextSample01() {
        return charge(new Sample01.Mutable());
    }

    public final Sample02 nextSample02() {
        return charge(new Sample02.Mutable());
    }

    public final List<Object> nextList() {
        return Stream.generate(this::nextString)
                     .limit(anyInt(4))
                     .collect(Collectors.toList());
    }
}
