package de.team33.patterns.json.charon;

import java.util.NoSuchElementException;
import java.util.stream.Stream;

public enum JsonCase {

    NULL(JsonNull.class),
    BOOLEAN(JsonBoolean.class),
    NUMBER(JsonNumber.class),
    STRING(JsonString.class),
    ARRAY(JsonArray.class),
    OBJECT(JsonObject.class);

    private final Class<? extends Json.Value> jsonType;

    JsonCase(final Class<? extends Json.Value> jsonType) {
        this.jsonType = jsonType;
    }

    public static JsonCase of(final Json.Value item) {
        return Stream.of(values())
                     .filter(value -> value.jsonType.isInstance(item))
                     .findAny()
                     .orElseThrow(() -> new NoSuchElementException(
                             "unexpected item type:%n%n    %s%n".formatted(type(item))));
    }

    @SuppressWarnings("ReturnOfNull")
    private static Class<?> type(final Json.Value item) {
        return (null == item) ? null : item.getClass();
    }
}
