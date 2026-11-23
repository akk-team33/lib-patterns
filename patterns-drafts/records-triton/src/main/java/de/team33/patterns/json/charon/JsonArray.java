package de.team33.patterns.json.charon;

import de.team33.patterns.collection.mneme.FinalList;
import de.team33.patterns.collection.mneme.ImmutableList;
import de.team33.patterns.streamable.galatea.Streamable;

import java.util.Collection;
import java.util.List;

class JsonArray extends JsonEntity<ImmutableList<Json.Value>> {

    JsonArray(final Collection<? extends Json.Value> source) {
        super(FinalList.of(source));
    }

    JsonArray(final Streamable<? extends Json.Value> source) {
        super(FinalList.of(source));
    }

    @Override
    public final Json.Type type() {
        return Json.Type.ARRAY;
    }

    @Override
    public final List<Json.Value> asList() {
        return value();
    }
}
