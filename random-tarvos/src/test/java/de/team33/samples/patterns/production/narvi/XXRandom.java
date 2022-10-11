package de.team33.samples.patterns.production.narvi;

import de.team33.patterns.random.tarvos.Charger;
import de.team33.patterns.random.tarvos.Generator;

import java.math.BigInteger;
import java.util.Random;

public class XXRandom extends Random implements Generator {

    private final Charger<XXRandom> charger = new Charger<>(XXRandom.class);

    @Override
    public final BigInteger nextBits(final int numBits) {
        return new BigInteger(numBits, this);
    }

    public final Sample nextSample() {
        return charger.charge(new Sample(), this);
    }

    public final Buildable nextBuildable() {
        return charger.charge(Buildable.builder(), this)
                      .build();
    }
}
