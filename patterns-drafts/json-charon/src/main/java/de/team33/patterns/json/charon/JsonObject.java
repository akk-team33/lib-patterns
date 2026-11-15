package de.team33.patterns.json.charon;

import de.team33.patterns.collection.mneme.FinalMap;
import de.team33.patterns.collection.mneme.ImmutableMap;

import java.util.Map;

public final class JsonObject extends JsonBase<ImmutableMap<String, JsonValue>> {

    public JsonObject(final Map<String, ? extends JsonValue> value) {
        super(FinalMap.of(value));
    }
}
