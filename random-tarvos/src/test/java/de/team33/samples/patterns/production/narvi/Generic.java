package de.team33.samples.patterns.production.narvi;

public class Generic<T> {

    private T tValue;

    public final T getTValue() {
        return tValue;
    }

    public final Generic<T> setTValue(final T tValue) {
        this.tValue = tValue;
        return this;
    }
}
