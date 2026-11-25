package de.team33.patterns.json.charon;

enum JsonNull implements Json.Value {

    INSTANCE;

    @Override
    public String toString() {
        return "null";
    }

    @Override
    public Json.Type type() {
        return Json.Type.NULL;
    }
}
