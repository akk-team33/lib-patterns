package de.team33.patterns.json.charon;

import de.team33.patterns.streamable.galatea.Buffer;
import de.team33.patterns.streamable.galatea.Streamable;

import java.util.AbstractList;
import java.util.List;
import java.util.RandomAccess;
import java.util.stream.Collector;

@SuppressWarnings("StaticInheritance")
public final class JsonArray extends AbstractList<JsonValue> implements RandomAccess, JsonValue {

    /**
     * A {@link Collector} to {@linkplain java.util.stream.Stream#collect(Collector) collect} elements of
     * {@link JsonValue} into a new {@link JsonArray}.
     */
    public static final Collector<JsonValue, ?, JsonArray> COLLECTOR = Buffer.collector(JsonArray::new);

    private final List<JsonValue> values;

    private JsonArray(final Streamable<JsonValue> buffer) {
        this.values = buffer.toList();
    }

    @Override
    public final JsonValue get(final int index) {
        return values.get(index);
    }

    @Override
    public final int size() {
        return values.size();
    }
}
