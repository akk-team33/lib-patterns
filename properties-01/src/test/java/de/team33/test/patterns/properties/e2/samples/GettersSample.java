package de.team33.test.patterns.properties.e2.samples;

import de.team33.patterns.properties.e2.Getters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class GettersSample {

    private static final
    Getters<GettersSample> GETTERS = Getters.add("intProperty", GettersSample::getIntProperty)
                                            .add("stringProperty", GettersSample::getStringProperty)
                                            .add("listProperty", GettersSample::getListProperty)
                                            .build();

    // Significant properties ...
    private final int intProperty;
    private final String stringProperty;
    private final List<Double> listProperty;

    public GettersSample(final int intProperty, final String stringProperty, final List<Double> listProperty) {
        this.intProperty = intProperty;
        this.stringProperty = stringProperty;
        this.listProperty = new ArrayList<>(listProperty);
    }

    private static Map<String, Object> toMap(final GettersSample sample) {
        final TreeMap<String, Object> result = GETTERS.<TreeMap<String, Object>>map(sample).to(new TreeMap<>());
        return result;
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
        return toMap(this).hashCode();
    }

    @Override
    public final boolean equals(final Object obj) {
        return (this == obj) ||
                ((obj instanceof GettersSample) && toMap(this).equals(toMap((GettersSample) obj)));
    }

    @Override
    public final String toString() {
        return toMap(this).toString();
    }

    public final Map<String, Object> toMap() {
        return toMap(this);
    }
}
