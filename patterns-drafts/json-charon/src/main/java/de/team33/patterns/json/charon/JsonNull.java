package de.team33.patterns.json.charon;

enum JsonNull implements JsonValue {

    INSTANCE;

    @Override
    public final Void value() {
        return null;
    }

    @Override
    public String toString() {
        return "null";
    }
}
