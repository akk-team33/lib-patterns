package de.team33.patterns.reflect.pandora.sample;

import de.team33.patterns.reflect.pandora.Getters;
import de.team33.patterns.reflect.pandora.Mapper;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static java.util.Objects.requireNonNull;

@SuppressWarnings({"ClassNamePrefixedWithPackageName", "unused", "InterfaceWithOnlyOneDirectInheritor"})
public interface Sample01 {

    int getIntValue();

    Long getLongValue();

    String getStringValue();

    Instant getInstantValue();

    List<Object> getListValue();

    @SuppressWarnings("ClassReferencesSubclass")
    default Mutable toMutable() {
        return Mutable.MAPPER.map(this, new Mutable());
    }

    @SuppressWarnings("AbstractClassWithOnlyOneDirectInheritor")
    abstract class Base implements Sample01 {

        private static final Getters<Sample01> GETTERS = Getters.of(Sample01.class);

        private Map<String, Object> toMap() {
            return GETTERS.toMap(this);
        }

        @Override
        public final int hashCode() {
            return toMap().hashCode();
        }

        @Override
        public final boolean equals(final Object obj) {
            return (this == obj) || ((obj instanceof Base) && toMap().equals(((Base) obj).toMap()));
        }

        @Override
        public final String toString() {
            return toMap().toString();
        }
    }

    class Mutable extends Base {

        private static final Mapper<Sample01, Mutable> MAPPER = Mapper.mapping(Base.GETTERS, Mutable.class);

        private int intValue;
        private Long longValue = 0L;
        private String stringValue = "";
        private Instant instantValue = Instant.now();
        private List<Object> listValue = Collections.emptyList();

        @Override
        public final int getIntValue() {
            return intValue;
        }

        public final Mutable setIntValue(final int intValue) {
            this.intValue = intValue;
            return this;
        }

        @Override
        public final Long getLongValue() {
            return longValue;
        }

        public final Mutable setLongValue(final Long longValue) {
            this.longValue = requireNonNull(longValue);
            return this;
        }

        @Override
        public final String getStringValue() {
            return stringValue;
        }

        public final Mutable setStringValue(final String stringValue) {
            this.stringValue = requireNonNull(stringValue);
            return this;
        }

        @Override
        public final Instant getInstantValue() {
            return instantValue;
        }

        public final Mutable setInstantValue(final Instant instantValue) {
            this.instantValue = requireNonNull(instantValue);
            return this;
        }

        @Override
        public final List<Object> getListValue() {
            return Collections.unmodifiableList(listValue);
        }

        public final Mutable setListValue(final List<Object> listValue) {
            this.listValue = new ArrayList<>(listValue);
            return this;
        }
    }
}
