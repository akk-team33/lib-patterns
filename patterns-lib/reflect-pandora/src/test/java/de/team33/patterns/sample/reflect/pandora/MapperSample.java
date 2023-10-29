package de.team33.patterns.sample.reflect.pandora;

import de.team33.patterns.reflect.pandora.Mapper;

import java.time.Instant;
import java.util.Map;

public interface MapperSample {

    static Simple simple(final MapperSample source) {
        return Simple.MAPPER.map(source, new Simple());
    }

    int getIntValue();

    Long getLongValue();

    String getStringValue();

    Instant getInstantValue();

    class Simple implements MapperSample {

        private static final Mapper<MapperSample, Simple> MAPPER = Mapper.mapping(MapperSample.class, Simple.class);

        private int intValue;
        private Long longValue;
        private String stringValue;
        private Instant instantValue;

        @Override
        public int getIntValue() {
            return intValue;
        }

        public Simple setIntValue(int intValue) {
            this.intValue = intValue;
            return this;
        }

        @Override
        public Long getLongValue() {
            return longValue;
        }

        public Simple setLongValue(Long longValue) {
            this.longValue = longValue;
            return this;
        }

        @Override
        public String getStringValue() {
            return stringValue;
        }

        public Simple setStringValue(String stringValue) {
            this.stringValue = stringValue;
            return this;
        }

        @Override
        public Instant getInstantValue() {
            return instantValue;
        }

        public Simple setInstantValue(Instant instantValue) {
            this.instantValue = instantValue;
            return this;
        }

        private Map<String, Object> toMap() {
            return MAPPER.toMap(this);
        }

        @Override
        public final int hashCode() {
            return toMap().hashCode();
        }

        @Override
        public final boolean equals(final Object obj) {
            return (this == obj) || ((obj instanceof Simple) && toMap().equals(((Simple) obj).toMap()));
        }

        @Override
        public final String toString() {
            return toMap().toString();
        }
    }
}
