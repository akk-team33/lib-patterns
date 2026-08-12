package de.team33.patterns.reflect.pandora.testing;

import de.team33.patterns.arbitrary.mimas.Charger;
import de.team33.patterns.arbitrary.mimas.Generator;
import de.team33.patterns.reflect.pandora.sample.Sample01;
import de.team33.patterns.reflect.pandora.sample.Sample02;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings({"unused", "WeakerAccess"})
public class Supply implements Generator, Charger {

    private final Random random = new SecureRandom();

    @Override
    public final BigInteger anyBits(final int numBits) {
        return new BigInteger(numBits, random);
    }

    public final Instant anyInstantValue() {
        return Instant.now().plusMillis(anyShort());
    }

    public final BeanClass anyBean() {
        return charge(new BeanClass());
    }

    public final Sample01 anySample01() {
        return charge(new Sample01.Mutable());
    }

    public final Sample02 anySample02() {
        return charge(new Sample02.Mutable());
    }

    public final List<Object> anyList() {
        return Stream.generate(this::anyString)
                     .limit(anyInt(4))
                     .collect(Collectors.toList());
    }
}
