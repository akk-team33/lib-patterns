package de.team33.patterns.records.rho;

interface JsonValue {

    JsonValue NULL = new JsonValue() {

        @Override
        public String toString() {
            return "%s.NULL".formatted(JsonValue.class.getSimpleName());
        }
    };
}
