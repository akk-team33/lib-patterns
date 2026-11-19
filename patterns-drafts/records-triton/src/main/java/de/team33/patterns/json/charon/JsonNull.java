package de.team33.patterns.json.charon;

enum JsonNull implements JsonItem {

    INSTANCE;

    @Override
    public String toString() {
        return "null";
    }
}
