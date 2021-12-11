package de.team33.samples.patterns.properties.e1;

import de.team33.patterns.properties.e1.Mapping;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class MappingSample {

    private static final Mapping<MappingSample> MAPPING =
            Mapping.add("intProperty", MappingSample::getIntProperty)
                   .add("stringProperty", MappingSample::getStringProperty)
                   .add("listProperty", MappingSample::getListProperty)
                   .build();

    // Significant properties ...
    private final int intProperty;
    private final String stringProperty;
    private final List<Double> listProperty;

    public MappingSample(final int intProperty, final String stringProperty, final List<Double> listProperty) {
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
        return MAPPING.toMap(this).hashCode();
    }

    @Override
    public final boolean equals(final Object obj) {
        return (this == obj)
                || ((obj instanceof MappingSample) && MAPPING.toMap(this).equals(MAPPING.toMap((MappingSample) obj)));
    }

    @Override
    public final String toString() {
        return MAPPING.toMap(this).toString();
    }

    public final Map<String, Object> toMap() {
        return MAPPING.toMap(this);
    }
}
