package de.team33.patterns.json.charon;

public class JsonString extends JsonEntity<String> {

    public JsonString(final String value) {
        super(value);
    }

    @Override
    public final Json.Type type() {
        return Json.Type.STRING;
    }
}
