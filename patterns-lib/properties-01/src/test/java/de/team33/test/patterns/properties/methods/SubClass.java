package de.team33.test.patterns.properties.methods;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.AbstractCollection;
import java.util.ArrayList;

import static java.util.Collections.singleton;

public class SubClass extends BaseClass implements X2, X3 {

    private static final String CLASS_NAME = SubClass.class.getCanonicalName();

    private ArrayList<String> first = new ArrayList<>(singleton(CLASS_NAME + ".getFirst()"));
    private String second = CLASS_NAME;

    @Override
    public final ArrayList<String> getFirst() {
        return first;
    }

    @Override
    public final SubClass setFirst(final AbstractCollection<String> value) {
        setFirst(new ArrayList<>(value));
        return this; //super.setFirst(value);
    }

    public final SubClass setFirst(final ArrayList<String> value) {
        first = new ArrayList<>(value);
        return this;
    }

    @Override
    public String getSecond() {
        return second;
    }

    @Override
    public void setSecond(final String value) {
        second = value;
    }

    public BigDecimal getThird() {
        return BigDecimal.valueOf(3.141592654);
    }

    @Override
    public void setThird(final Serializable value) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    public void setThird(final Number value) {
        throw new UnsupportedOperationException("not yet implemented");
    }
}
