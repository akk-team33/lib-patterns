package de.team33.sample.patterns.reflect.luna;

import de.team33.patterns.exceptional.e1.Converter;
import de.team33.patterns.exceptional.e1.Wrapping;
import de.team33.patterns.reflect.luna.Fields;

import java.lang.reflect.Field;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

public class Sample {

    private static final List<Field> FIELDS = Fields.listOf(Sample.class);
    private static final Converter CONVERTER = Converter.using(Wrapping.method(IllegalStateException::new));

    private int intValue;
    private Double doubleValue;
    private Instant instantValue;
    private String stringValue;

    public Sample() {
    }

    public Sample(final Sample origin) {
        FIELDS.forEach(CONVERTER.consumer(field -> field.set(this, field.get(origin))));
    }

    public int getIntValue() {
        return intValue;
    }

    public Double getDoubleValue() {
        return doubleValue;
    }

    public Instant getInstantValue() {
        return instantValue;
    }

    public String getStringValue() {
        return stringValue;
    }

    public List<Object> toList() {
        return FIELDS.stream()
                     .map(CONVERTER.function(field -> field.get(this)))
                     .collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || (obj instanceof Sample && toList().equals(((Sample)obj).toList()));
    }

    @Override
    public int hashCode() {
        return toList().hashCode();
    }

    @Override
    public String toString() {
        return toList().toString();
    }

    public static class Builder {

        private final Sample backing = new Sample();

        public Builder setIntValue(int intValue) {
            backing.intValue = intValue;
            return this;
        }

        public Builder setDoubleValue(Double doubleValue) {
            backing.doubleValue = doubleValue;
            return this;
        }

        public Builder setInstantValue(Instant instantValue) {
            backing.instantValue = instantValue;
            return this;
        }

        public Builder setStringValue(String stringValue) {
            backing.stringValue = stringValue;
            return this;
        }

        public Sample build() {
            return new Sample(backing);
        }
    }
}
