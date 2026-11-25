package de.team33.patterns.json.charon;

final class JsonString extends JsonItem<String> {

    JsonString(final String value) {
        super(value);
    }

    @Override
    public Json.Type type() {
        return Json.Type.STRING;
    }

    @Override
    public String asString() {
        return value();
    }
}
