package de.team33.patterns.decision.telesto.sample;

// Type of input parameter for criterion k2 ...
public class Y {

    private final int value;

    public Y(final int value) {
        this.value = value;
    }

    public boolean k2() {
        return 0 < value;
    }
}
