package de.team33.test.patterns.properties.e2.samples;

import de.team33.patterns.properties.e2.Fields;
import de.team33.patterns.properties.e2.Helper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class HelperSample {

    private static final Fields<HelperSample> FIELDS =
            Fields.of(HelperSample.class, Fields.Mode.STRAIGHT);
    private static final Helper<HelperSample> HELPER =
            new Helper<>(sample -> FIELDS.analyse(sample, new TreeMap<>()));

    // Significant properties ...
    private final int intProperty;
    private final String stringProperty;
    private final List<Double> listProperty;

    public HelperSample(final int intProperty, final String stringProperty, final List<Double> listProperty) {
        this.intProperty = intProperty;
        this.stringProperty = stringProperty;
        this.listProperty = new ArrayList<>(listProperty);
    }

    public final int getIntProperty() {
        return intProperty;
    }

    public final String getStringProperty() {
        return stringProperty;
    }

    public final List<Double> getListProperty() {
        return Collections.unmodifiableList(listProperty);
    }

    @Override
    public final int hashCode() {
        return HELPER.hashCode(this);
    }

    @Override
    public final boolean equals(final Object obj) {
        return (this == obj) || ((obj instanceof HelperSample) && HELPER.equals(this, (HelperSample) obj));
    }

    @Override
    public final String toString() {
        return HELPER.toString(this);
    }

    public final Map<String, Object> toMap() {
        return HELPER.toMap(this);
    }
}
