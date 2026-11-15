package de.team33.patterns.json.charon;

import de.team33.patterns.collection.mneme.FinalList;
import de.team33.patterns.collection.mneme.ImmutableList;

import java.util.Collection;

public final class JsonArray extends JsonBase<ImmutableList<JsonValue>> {

    public JsonArray(final Collection<? extends JsonValue> value) {
        super(FinalList.of(value));
    }
}
