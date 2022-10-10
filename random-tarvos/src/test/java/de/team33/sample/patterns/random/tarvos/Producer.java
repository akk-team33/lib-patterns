package de.team33.sample.patterns.random.tarvos;

import de.team33.patterns.random.tarvos.Generator;
import de.team33.patterns.random.tarvos.Loader;

import java.math.BigInteger;
import java.util.Random;

public class Producer extends Random implements Generator {

    private final Loader<Producer> loader = new Loader<>(Producer.class);

    @Override
    public final BigInteger nextBits(final int numBits) {
        return new BigInteger(numBits, this);
    }

    public final Person nextPerson() {
        return loader.load(new Person(), this);
    }

    public final Customer nextCustomer() {
        return loader.load(new Customer(), this);
    }

    public final Employee nextEmployee() {
        return loader.load(new Employee(), this);
    }
}
