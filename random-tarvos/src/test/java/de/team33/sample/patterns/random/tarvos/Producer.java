package de.team33.sample.patterns.random.tarvos;

import de.team33.patterns.random.tarvos.Generator;
import de.team33.patterns.random.tarvos.Charger;

import java.math.BigInteger;
import java.util.Random;

public class Producer extends Random implements Generator {

    private final Charger<Producer> charger = new Charger<>(Producer.class);

    @Override
    public final BigInteger nextBits(final int numBits) {
        return new BigInteger(numBits, this);
    }

    public final Person nextPerson() {
        return charger.charge(new Person(), this);
    }

    public final Customer nextCustomer() {
        return charger.charge(new Customer(), this);
    }

    public final Employee nextEmployee() {
        return charger.charge(new Employee(), this);
    }
}
