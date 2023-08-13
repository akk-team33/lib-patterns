package de.team33.sample.patterns.random.mimas;

import de.team33.patterns.random.mimas.Extension;

import java.math.BigInteger;
import java.util.Random;

public class XRandom extends Random implements Extension {

    @Override
    public final BigInteger nextBits(final int numBits) {
        return new BigInteger(numBits, this);
    }
}
