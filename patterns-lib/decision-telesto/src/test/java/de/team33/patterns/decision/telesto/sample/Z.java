package de.team33.patterns.decision.telesto.sample;

// Type of input parameter for criterion k3 ...
public class Z {

    private final int value;

    public Z(int value) {
        this.value = value;
    }

    public boolean k3() {
        return 0 == value;
    }
}
