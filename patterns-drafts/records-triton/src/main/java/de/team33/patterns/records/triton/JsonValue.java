package de.team33.patterns.records.triton;

interface JsonValue {

    JsonValue NULL = new JsonValue() {

        @Override
        public String toString() {
            return "%s.NULL".formatted(JsonValue.class.getSimpleName());
        }
    };
}
