package de.team33.patterns.building.elara.sample;

import de.team33.patterns.building.elara.DataBuilder;
import de.team33.patterns.reflect.luna.Fields;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public final class Buildable {

    private static final Fields FIELDS = Fields.of(Buildable.class);

    private int intValue;
    private Double doubleValue;
    private Instant instantValue;
    private String stringValue;

    @SuppressWarnings("CopyConstructorMissesField")
    private Buildable(final Buildable origin) {
        if (null != origin) {
            FIELDS.forEach(field -> field.set(this, field.get(origin)));
        }
    }

    public static Builder builder() {
        return new Builder(null);
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
        return FIELDS.map(field -> field.get(this))
                     .collect(Collectors.toList());
    }

    @Override
    public final boolean equals(final Object obj) {
        return (this == obj) || ((obj instanceof final Buildable other) && toList().equals(other.toList()));
    }

    @Override
    public final int hashCode() {
        return toList().hashCode();
    }

    @Override
    public final String toString() {
        return toList().toString();
    }

    public final Builder toBuilder() {
        return new Builder(this);
    }

    public static final class Builder extends DataBuilder<Buildable, Buildable, Builder> {

        private Builder(final Buildable origin) {
            super(new Buildable(origin), Buildable::new, Builder.class);
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
    }
}
