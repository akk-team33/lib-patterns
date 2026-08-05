package de.team33.patterns.pooling.ariel.publics;

public class Dispenser {

    private final int value;

    public Dispenser(final int value) {
        this.value = value;
    }

    public final int getValue() {
        return value;
    }
}
