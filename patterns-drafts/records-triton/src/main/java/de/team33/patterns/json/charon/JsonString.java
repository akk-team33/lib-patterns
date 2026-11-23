package de.team33.patterns.json.charon;

class JsonString extends JsonEntity<String> {

    JsonString(final String value) {
        super(value);
    }

    @Override
    public final Json.Type type() {
        return Json.Type.STRING;
    }

    @Override
    public final String asString() {
        return value();
    }
}
