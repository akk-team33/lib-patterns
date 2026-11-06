package de.team33.patterns.json.charon;

public interface JsonValue {

    JsonValue NULL = new JsonValue() {

        @Override
        public String toString() {
            return "null";
        }
    };
}
