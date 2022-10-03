package de.team33.sample.patterns.random.tarvos;

import de.team33.patterns.random.tarvos.Producer;

import java.math.BigInteger;
import java.util.Random;

public class XRandom extends Random implements Producer {

    @Override
    public final BigInteger nextBits(final int numBits) {
        return new BigInteger(numBits, this);
    }
}
