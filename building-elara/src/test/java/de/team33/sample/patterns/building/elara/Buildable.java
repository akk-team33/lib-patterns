package de.team33.sample.patterns.building.elara;

import de.team33.patterns.building.elara.Mutable;
import de.team33.patterns.exceptional.e1.Converter;
import de.team33.patterns.exceptional.e1.Wrapping;

import java.lang.reflect.Field;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Buildable {

    private static final Converter CONVERTER = Converter.using(Wrapping.method(IllegalStateException::new));
    private static final List<Field> FIELDS = Stream.of(Buildable.class.getDeclaredFields())
                                                    .filter(Fields::significant)
                                                    .collect(Collectors.toList());

    private int intValue;
    private Double doubleValue;
    private Instant instantValue;
    private String stringValue;

    public Buildable(final int intValue,
                     final Double doubleValue,
                     final Instant instantValue,
                     final String stringValue) {
        this.intValue = intValue;
        this.doubleValue = doubleValue;
        this.instantValue = instantValue;
        this.stringValue = stringValue;
    }

    public Buildable(final Buildable origin) {
        FIELDS.forEach(CONVERTER.consumer(field -> field.set(this, field.get(origin))));
    }

    public final int getIntValue() {
        return intValue;
    }

    public final Double getDoubleValue() {
        return doubleValue;
    }

    public final Instant getInstantValue() {
        return instantValue;
    }

    public final String getStringValue() {
        return stringValue;
    }

    public final List<Object> toList() {
        return FIELDS.stream()
                     .map(CONVERTER.function(field -> field.get(this)))
                     .collect(Collectors.toList());
    }

    @Override
    public final boolean equals(final Object o) {
        return (this == o) || ((o instanceof Buildable) && toList().equals(((Buildable) o).toList()));
    }

    @Override
    public final int hashCode() {
        return toList().hashCode();
    }

    @Override
    public final String toString() {
        return toList().toString();
    }

    public static final class Builder extends Mutable.Builder<Buildable, Builder> {

        private Builder() {
            super(new Buildable(0, null, null, null), Builder.class);
        }

        public final Builder setIntValue(final int value) {
            return setup(subject -> subject.intValue = value);
        }

        public final Builder setDoubleValue(final Double value) {
            return setup(subject -> subject.doubleValue = value);
        }

        public final Builder setInstantValue(final Instant value) {
            return setup(subject -> subject.instantValue = value);
        }

        public final Builder setStringValue(final String value) {
            return setup(subject -> subject.stringValue = value);
        }

        public final Buildable build() {
            return new Buildable(subject());
        }
    }
}
