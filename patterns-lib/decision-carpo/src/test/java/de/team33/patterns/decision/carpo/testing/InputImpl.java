package de.team33.patterns.decision.carpo.testing;

public class InputImpl implements Input {

    private final boolean a;
    private final boolean b;
    private final boolean c;

    @SuppressWarnings("BooleanParameter")
    public InputImpl(final boolean a, final boolean b, final boolean c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public InputImpl(final int bitSet) {
        this.a = (bitSet & 1) == 1;
        this.b = (bitSet & 2) == 2;
        this.c = (bitSet & 4) == 4;
    }

    @Override
    public final boolean isA() {
        return a;
    }

    @Override
    public final boolean isB() {
        return b;
    }

    @Override
    public final boolean isC() {
        return c;
    }
}
