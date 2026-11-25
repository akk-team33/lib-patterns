package de.team33.patterns.json.charon;

import de.team33.patterns.collection.mneme.FinalMap;
import de.team33.patterns.collection.mneme.ImmutableMap;
import de.team33.patterns.streamable.galatea.Streamable;

import java.util.Map;

final class JsonObject extends JsonItem<ImmutableMap<String, Json.Value>> {

    JsonObject(final Map<String, ? extends Json.Value> source) {
        super(FinalMap.of(source));
    }

    JsonObject(final Streamable<? extends Map.Entry<String, ? extends Json.Value>> source) {
        super(FinalMap.of(source));
    }

    @Override
    public Json.Type type() {
        return Json.Type.OBJECT;
    }

    @Override
    public Map<String, Json.Value> asMap() {
        return value();
    }
}
