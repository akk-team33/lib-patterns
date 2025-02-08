package de.team33.patterns.decision.leda.sample;

// Type of input parameter for criterion k1 ...
public class X {

    private final int value;

    public X(int value) {
        this.value = value;
    }

    public boolean k1() {
        return 0 > value;
    }
}
