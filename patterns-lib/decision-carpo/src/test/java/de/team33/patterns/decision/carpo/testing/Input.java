package de.team33.patterns.decision.carpo.testing;

public class Input {

    private final boolean a;
    private final boolean b;
    private final boolean c;

    public Input(final boolean a, final boolean b, final boolean c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public Input(final int bitSet) {
        this.a = (bitSet & 1) == 1;
        this.b = (bitSet & 2) == 2;
        this.c = (bitSet & 4) == 4;
    }

    public boolean isA() {
        return a;
    }

    public boolean isB() {
        return b;
    }

    public boolean isC() {
        return c;
    }
}
