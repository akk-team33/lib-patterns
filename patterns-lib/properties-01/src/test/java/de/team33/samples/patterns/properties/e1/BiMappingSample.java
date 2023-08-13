package de.team33.samples.patterns.properties.e1;

import de.team33.patterns.properties.e1.BiMapping;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class BiMappingSample {

    private static final BiMapping<BiMappingSample> MAPPING =
            BiMapping.add("intProperty", BiMappingSample::getIntProperty, BiMappingSample::setIntProperty)
                     .add("stringProperty", BiMappingSample::getStringProperty, BiMappingSample::setStringProperty)
                     .add("listProperty", BiMappingSample::getListProperty, BiMappingSample::setListProperty)
                     .build();

    // Significant properties ...
    private int intProperty;
    private String stringProperty;
    private List<Double> listProperty;

    public BiMappingSample(final Map<?, ?> origin) {
        MAPPING.remap(origin).to(this);
    }

    public BiMappingSample(final BiMappingSample origin) {
        MAPPING.copy(origin).to(this);
    }

    public BiMappingSample(final int intProperty, final String stringProperty, final List<Double> listProperty) {
        this.intProperty = intProperty;
        this.stringProperty = stringProperty;
        this.listProperty = new ArrayList<>(listProperty);
    }

    public final int getIntProperty() {
        return intProperty;
    }

    public final BiMappingSample setIntProperty(final int intProperty) {
        this.intProperty = intProperty;
        return this;
    }

    public final String getStringProperty() {
        return stringProperty;
    }

    public final BiMappingSample setStringProperty(final String stringProperty) {
        this.stringProperty = stringProperty;
        return this;
    }

    public final List<Double> getListProperty() {
        return Collections.unmodifiableList(listProperty);
    }

    public final BiMappingSample setListProperty(final List<Double> listProperty) {
        this.listProperty = listProperty;
        return this;
    }

    @Override
    public final int hashCode() {
        return MAPPING.toMap(this).hashCode();
    }

    @Override
    public final boolean equals(final Object obj) {
        return (this == obj)
               || ((obj instanceof BiMappingSample) && MAPPING.toMap(this).equals(MAPPING.toMap((BiMappingSample) obj)));
    }

    @Override
    public final String toString() {
        return MAPPING.toMap(this).toString();
    }

    public final Map<String, Object> toMap() {
        return MAPPING.toMap(this);
    }
}
