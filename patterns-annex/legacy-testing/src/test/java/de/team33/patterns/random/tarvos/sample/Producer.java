package de.team33.patterns.random.tarvos.sample;

import de.team33.patterns.random.tarvos.Charger;
import de.team33.patterns.random.tarvos.Generator;
import de.team33.patterns.random.tarvos.Initiator;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

public class Producer implements Generator, Charger, Initiator {

    private final Random random = new SecureRandom();

    @Override
    public final BigInteger nextBits(final int numBits) {
        return new BigInteger(numBits, random);
    }

    public final Person nextPerson() {
        return initiate(Person.class);
    }

    public final Customer nextCustomer() {
        return charge(new Customer());
    }

    public final Employee nextEmployee() {
        return charge(new Employee());
    }
}
