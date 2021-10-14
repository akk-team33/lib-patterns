package de.team33.test.patterns.properties.e3.samples;

import de.team33.patterns.properties.e3.BasiX;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BasiXSample {

    private static final BasiX<BasiXSample> BASIX = new BasiX<>(BasiXSample.class, Mapping::toMap);

    private final int intProp;
    private final String strProp;
    private final List<Double> listProp;

    public BasiXSample(final int intProp, final String strProp, final List<Double> listProp) {
        this.intProp = intProp;
        this.strProp = strProp;
        this.listProp = new ArrayList<>(listProp);
    }

    public final int getIntProp() {
        return intProp;
    }

    public final String getStrProp() {
        return strProp;
    }

    public final List<Double> getListProp() {
        return listProp;
    }

    @Override
    public final int hashCode() {
        return BASIX.hashCode(this);
    }

    @Override
    public final boolean equals(final Object obj) {
        return BASIX.equals(this, obj);
    }

    @Override
    public final String toString() {
        return BASIX.toString(this);
    }

    public final Map<String, Object> toMap() {
        return BASIX.toMap(this);
    }
}
