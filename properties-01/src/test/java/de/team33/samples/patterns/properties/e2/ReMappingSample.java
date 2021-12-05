package de.team33.samples.patterns.properties.e2;

import de.team33.patterns.properties.e2.ReMapping;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ReMappingSample {

    private static final ReMapping<ReMappingSample> MAPPING =
            ReMapping.add("intProperty", ReMappingSample::setIntProperty)
                     .add("stringProperty", ReMappingSample::setStringProperty)
                     .add("listProperty", ReMappingSample::setListProperty)
                     .build();

    // Significant properties ...
    private int intProperty;
    private String stringProperty;
    private List<Double> listProperty;

    public ReMappingSample(final Map<?, ?> origin) {
        MAPPING.remap(origin).to(this);
    }

    public ReMappingSample(final int intProperty, final String stringProperty, final List<Double> listProperty) {
        this.intProperty = intProperty;
        this.stringProperty = stringProperty;
        this.listProperty = new ArrayList<>(listProperty);
    }

    public final int getIntProperty() {
        return intProperty;
    }

    public ReMappingSample setIntProperty(final int intProperty) {
        this.intProperty = intProperty;
        return this;
    }

    public final String getStringProperty() {
        return stringProperty;
    }

    public ReMappingSample setStringProperty(final String stringProperty) {
        this.stringProperty = stringProperty;
        return this;
    }

    public final List<Double> getListProperty() {
        return Collections.unmodifiableList(listProperty);
    }

    public ReMappingSample setListProperty(final List<Double> listProperty) {
        this.listProperty = listProperty;
        return this;
    }
}
