package de.team33.patterns.json.charon;

import de.team33.patterns.collection.mneme.FinalMap;
import de.team33.patterns.collection.mneme.ImmutableMap;
import de.team33.patterns.streamable.galatea.Buffer;
import de.team33.patterns.streamable.galatea.Streamable;

import java.util.Map;
import java.util.stream.Collector;

public final class JsonObject extends JsonUnit<ImmutableMap<String, JsonItem>> {

    public JsonObject(final Map<String, ? extends JsonItem> source) {
        super(FinalMap.of(source));
    }

    public JsonObject(final Streamable<? extends Map.Entry<String, ? extends JsonItem>> source) {
        super(FinalMap.of(source));
    }

    public static Collector<Map.Entry<String, ? extends JsonItem>, ?, JsonObject> collector() {
        return Buffer.collector(JsonObject::new);
    }
}
