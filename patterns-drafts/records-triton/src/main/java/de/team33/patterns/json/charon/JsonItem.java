package de.team33.patterns.json.charon;

public interface JsonItem {

    JsonItem NULL = new JsonItem() {

        @Override
        public String toString() {
            return "%s.NULL".formatted(JsonItem.class.getSimpleName());
        }
    };
}
