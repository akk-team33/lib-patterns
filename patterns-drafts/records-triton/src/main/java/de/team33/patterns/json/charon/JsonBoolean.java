package de.team33.patterns.json.charon;

final class JsonBoolean extends JsonItem<Boolean> {

    JsonBoolean(final Boolean value) {
        super(value);
    }

    @Override
    public Json.Type type() {
        return Json.Type.BOOLEAN;
    }

    @Override
    public boolean asBoolean() {
        return value();
    }
}
