package de.team33.patterns.decision.carpo.sample;

public class InputImpl implements Input {

    private final int i;
    private final int k;
    private final int n;

    public InputImpl(final int i, final int k, final int n) {
        this.i = i;
        this.k = k;
        this.n = n;
    }

    @Override
    public boolean isConditionOne() {
        return i == 0;
    }

    @Override
    public boolean isConditionTwo() {
        return k == 0;
    }

    @Override
    public boolean isConditionThree() {
        return n == 0;
    }
}
