package de.team33.patterns.json.charon;

@SuppressWarnings("StaticInheritance")
public enum JsonBoolean implements JsonValue {

    FALSE,
    TRUE;

    public static JsonBoolean of(final boolean value) {
        return value ? TRUE : FALSE;
    }

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
