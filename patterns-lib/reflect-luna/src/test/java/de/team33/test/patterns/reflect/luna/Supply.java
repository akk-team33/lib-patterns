package de.team33.test.patterns.reflect.luna;

import de.team33.patterns.random.tarvos.Charger;
import de.team33.patterns.random.tarvos.Generator;
import de.team33.sample.patterns.reflect.luna.Level0;
import de.team33.sample.patterns.reflect.luna.Level1;
import de.team33.sample.patterns.reflect.luna.Level2;
import de.team33.sample.patterns.reflect.luna.Simple;

import java.awt.GridBagConstraints;
import java.math.BigInteger;
import java.time.Instant;
import java.util.Random;

public class Supply extends Random implements Generator, Charger {

    @Override
    public final BigInteger nextBits(int numBits) {
        return new BigInteger(numBits, this);
    }

    public final Instant nextInstant() {
        return Instant.now()
                      .plusSeconds(nextLong(-100000L, 100000L));
    }

    public final String nextString() {
        return nextString(nextInt(0, 24), "abc");
    }

    public final Simple nextSimple() {
        return charge(new Simple());
    }

    public final Level0 nextLevel0() {
        return charge(new Level0());
    }

    public final Level1 nextLevel1() {
        return charge(new Level1());
    }

    public final Level2 nextLevel2() {
        return new Level2(nextLevel1()).setIntValue2(nextInt())
                                       .setDoubleValue2(nextDouble())
                                       .setInstantValue2(nextInstant())
                                       .setStringValue2(nextString());
    }

    public GridBagConstraints nextGridBagConstraints() {
        return new GridBagConstraints(nextInt(8),
                                      nextInt(8),
                                      nextInt(16),
                                      nextInt(16),
                                      nextDouble(),
                                      nextDouble(),
                                      nextInt(4),
                                      nextInt(4),
                                      null,
                                      nextInt(5),
                                      nextInt(5));
    }
}
