package de.team33.patterns.json.charon;

public class JsonBoolean extends JsonEntity<Boolean> {

    public JsonBoolean(final Boolean value) {
        super(value);
    }

    @Override
    public final Json.Type type() {
        return Json.Type.BOOLEAN;
    }
}
