package de.team33.samples.patterns.production.narvi;

import de.team33.patterns.random.tarvos.Loader;
import de.team33.patterns.random.tarvos.Generator;

import java.math.BigInteger;
import java.util.Random;

public class XXRandom extends Random implements Generator {

    private final Loader<XXRandom> loader = new Loader<>(XXRandom.class);

    @Override
    public final BigInteger nextBits(final int numBits) {
        return new BigInteger(numBits, this);
    }

    public final Sample nextSample() {
        return loader.load(new Sample(), this);
    }

    public final Buildable nextBuildable() {
        return loader.load(Buildable.builder(), this)
                     .build();
    }
}
