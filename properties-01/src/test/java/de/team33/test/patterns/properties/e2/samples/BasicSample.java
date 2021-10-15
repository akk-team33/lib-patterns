package de.team33.test.patterns.properties.e2.samples;

import de.team33.patterns.properties.e2.Basics;
import de.team33.patterns.properties.e2.Fields;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;

public class BasicSample {

    private static final Fields<BasicSample> FIELDS =
            Fields.of(BasicSample.class, Fields.Mode.STRAIGHT);
    private static final Function<BasicSample, Basics> BASICS =
            Basics.factory(sample -> FIELDS.analyse(sample, new TreeMap<>()));

    // Significant properties ...
    private final int intProperty;
    private final String stringProperty;
    private final List<Double> listProperty;

    // Declared as <transient> to be ignored as non-significant property ...
    private final transient Basics basics = BASICS.apply(this);

    public BasicSample(final int intProperty, final String stringProperty, final List<Double> listProperty) {
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
        return basics.hashView();
    }

    @Override
    public final boolean equals(final Object obj) {
        return (this == obj) || ((obj instanceof BasicSample) && basics.isEqual(((BasicSample) obj).basics));
    }

    @Override
    public final String toString() {
        return basics.stringView();
    }

    public final Map<String, Object> toMap() {
        return basics.mapView();
    }
}
