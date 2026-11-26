package de.team33.patterns.json.charon;

import de.team33.patterns.collection.mneme.FinalList;
import de.team33.patterns.collection.mneme.ImmutableList;
import de.team33.patterns.streamable.galatea.Streamable;

import java.util.Collection;
import java.util.List;

final class JsonArray extends JsonItem<ImmutableList<Json.Value>> {

    JsonArray(final Collection<? extends Json.Value> source) {
        super(FinalList.by(source));
    }

    JsonArray(final Streamable<? extends Json.Value> source) {
        super(FinalList.by(source));
    }

    @Override
    public Json.Type type() {
        return Json.Type.ARRAY;
    }

    @Override
    public List<Json.Value> asList() {
        return value();
    }
}
