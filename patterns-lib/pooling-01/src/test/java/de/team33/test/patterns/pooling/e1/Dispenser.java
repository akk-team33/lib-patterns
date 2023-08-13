package de.team33.test.patterns.pooling.e1;

public class Dispenser {

    private final int value;

    public Dispenser(final int value) {
        this.value = value;
    }

    public final int getValue() {
        return value;
    }
}
