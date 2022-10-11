package de.team33.samples.patterns.production.narvi;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Generic<T, U, V> {
    private boolean booleanValue;
    private int intValue;
    private T tValue;
    private U uValue;
    private V vValue;
    private List<T> tList;
    private Map<U, V> uvMap;

    public static void setNothing(final Object nothing) {
        throw new UnsupportedOperationException("should not be called anyways");
    }

    @SuppressWarnings("rawtypes")
    private static List asList(final Generic g) {
        return Arrays.asList(
                g.booleanValue,
                g.intValue,
                g.tValue,
                g.uValue,
                g.vValue,
                g.tList,
                g.uvMap);
    }

    public final T getTValue() {
        return tValue;
    }

    public final Generic<T, U, V> setTValue(final T tValue) {
        this.tValue = tValue;
        return this;
    }

    public final U getUValue() {
        return uValue;
    }

    public final Generic<T, U, V> setUValue(final U uValue) {
        this.uValue = uValue;
        return this;
    }

    public final V getVValue() {
        return vValue;
    }

    public final Generic<T, U, V> setVValue(final V vValue) {
        this.vValue = vValue;
        return this;
    }

    public final List<T> getTList() {
        return tList;
    }

    public final Generic<T, U, V> setTList(final List<T> tList) {
        this.tList = tList;
        return this;
    }

    public final Map<U, V> getUvMap() {
        return uvMap;
    }

    public final Generic<T, U, V> setUvMap(final Map<U, V> uvMap) {
        this.uvMap = uvMap;
        return this;
    }

    @Override
    public final boolean equals(final Object o) {
        return (this == o) || ((o instanceof Generic) && asList(this).equals(asList((Generic) o)));
    }

    @Override
    public final int hashCode() {
        return asList(this).hashCode();
    }

    @Override
    public final String toString() {
        return asList(this).toString();
    }

    public final int getIntValue() {
        return intValue;
    }

    public final Generic<T, U, V> setIntValue(final int intValue) {
        this.intValue = intValue;
        return this;
    }

    public final boolean isBooleanValue() {
        return booleanValue;
    }

    public final Generic<T, U, V> setBooleanValue(final boolean booleanValue) {
        this.booleanValue = booleanValue;
        return this;
    }
}
