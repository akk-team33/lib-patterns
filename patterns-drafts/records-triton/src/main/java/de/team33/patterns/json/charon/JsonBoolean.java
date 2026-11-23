package de.team33.patterns.json.charon;

class JsonBoolean extends JsonEntity<Boolean> {

    JsonBoolean(final Boolean value) {
        super(value);
    }

    @Override
    public final Json.Type type() {
        return Json.Type.BOOLEAN;
    }

    @Override
    public final boolean asBoolean() {
        return value();
    }
}
