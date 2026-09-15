package de.team33.patterns.arbitrary.mimas.sample;

import de.team33.patterns.arbitrary.mimas.Charger;
import de.team33.patterns.arbitrary.mimas.Generator;
import de.team33.patterns.arbitrary.mimas.Initiator;

import java.math.BigInteger;
import java.util.Random;

@SuppressWarnings("unused")
public class Producer extends Random implements Generator, Charger, Initiator {

    @Override
    public final BigInteger anyBits(final int numBits) {
        return new BigInteger(numBits, this);
    }

    public final Person anyPerson() {
        return initiate(Person.class);
    }

    public final Customer anyCustomer() {
        return charge(new Customer());
    }

    public final Employee anyEmployee() {
        return charge(new Employee());
    }
}
