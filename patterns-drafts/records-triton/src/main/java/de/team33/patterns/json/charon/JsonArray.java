package de.team33.patterns.json.charon;

import de.team33.patterns.collection.mneme.FinalList;
import de.team33.patterns.collection.mneme.ImmutableList;
import de.team33.patterns.streamable.galatea.Buffer;
import de.team33.patterns.streamable.galatea.Streamable;

import java.util.Collection;
import java.util.stream.Collector;
import java.util.stream.Stream;

public final class JsonArray extends JsonEntity<ImmutableList<Json.Value>> {

    public JsonArray(final Collection<? extends Json.Value> source) {
        super(FinalList.of(source));
    }

    public JsonArray(final Streamable<? extends Json.Value> source) {
        super(FinalList.of(source));
    }

    /**
     * Returns a {@link Collector} to {@linkplain Stream#collect(Collector) collect} {@link Json.Value}s
     * into a new {@link JsonArray}.
     */
    public static Collector<Json.Value, ?, JsonArray> collector() {
        return Buffer.collector(JsonArray::new);
    }
}
