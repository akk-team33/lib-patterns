package de.team33.test.patterns.properties.e3.samples;

import de.team33.patterns.properties.e3.Basics;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BasicsSample {

    private static final Basics<BasicsSample> BASICS = new Basics<>(Mapping::toMap);

    private final int intProp;
    private final String strProp;
    private final List<Double> listProp;

    public BasicsSample(final int intProp, final String strProp, final List<Double> listProp) {
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
        return BASICS.hashCode(this);
    }

    @Override
    public final boolean equals(final Object obj) {
        return this == obj || (obj instanceof BasicsSample && BASICS.equals(this, (BasicsSample) obj));
    }

    @Override
    public final String toString() {
        return BASICS.toString(this);
    }

    public final Map<String, Object> toMap() {
        return BASICS.toMap(this);
    }
}
