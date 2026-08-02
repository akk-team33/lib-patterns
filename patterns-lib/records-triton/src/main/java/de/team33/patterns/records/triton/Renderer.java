package de.team33.patterns.records.triton;

import de.team33.patterns.enums.pan.Values;

import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

final class Renderer {

    private static final String NEWLINE = "%n".formatted();

    private final StringBuilder target;

    private Renderer() {
        this.target = new StringBuilder();
    }

    static String render(final JsonValue source) {
        return new Renderer().add(source, 0)
                             .toString();
    }

    private Renderer add(final String source) {
        target.append(source);
        return this;
    }

    private Renderer addNewLine(final int indentation) {
        return add(NEWLINE).addSpace(indentation);
    }

    private Renderer addSpace(final int indentation) {
        return add(Stream.generate(() -> "   ")
                         .limit(indentation)
                         .collect(joining()));
    }

    private Renderer add(final JsonValue source, final int indentation) {
        if (JsonValue.NULL == source) {
            return add("null");
        } else {
            final JsonType type = JsonType.of(source.getClass());
            return type.render(this, source, indentation);
        }
    }

    @Override
    public final String toString() {
        return target.toString();
    }

    private Renderer addObject(final JsonObject source, final int indentation) {
        return add("{").addObjectBody(source, indentation + 1).add("}");
    }

    private Renderer addObjectBody(final JsonObject source, final int indentation) {
        final int size = source.size();
        if (0 < size) {
            for (int index = 0; index < size; index++) {
                if (0 < index) {
                    add(",");
                }
                addNewLine(indentation);
                final JsonObject.Entry entry = source.get(index);
                add(StringLiteral.render(entry.name())).add(" : ").add(entry.value(), indentation);
            }
            return addNewLine(indentation - 1);
        } else {
            return this;
        }
    }

    private Renderer addArray(final JsonArray source, final int indentation) {
        return add("[").addArrayBody(source, indentation).add("]");
    }

    private Renderer addArrayBody(final JsonArray source, final int indentation) {
        for (int index = 0; index < source.size(); ++index) {
            if (0 < index) {
                add(", ");
            }
            add(source.get(index), indentation);
        }
        return this;
    }

    private Renderer addString(final JsonString source, final int indentation) {
        return add(StringLiteral.render(source.value()));
    }

    private Renderer addNumber(final JsonNumber source, final int indentation) {
        return add(source.value().toString());
    }

    private Renderer addBoolean(final JsonBoolean source, final int indentation) {
        return add(Boolean.toString(source.value()));
    }

    private enum JsonType {

        BOOLEAN(JsonBoolean.class, Renderer::addBoolean),
        NUMBER(JsonNumber.class, Renderer::addNumber),
        STRING(JsonString.class, Renderer::addString),
        ARRAY(JsonArray.class, Renderer::addArray),
        OBJECT(JsonObject.class, Renderer::addObject);

        private static final Values<JsonType> VALUES = Values.of(JsonType.class);

        private final Class<? extends JsonValue> jsonType;
        @SuppressWarnings("rawtypes")
        private final Rendering rendering;

        <T extends JsonValue> JsonType(final Class<T> jsonType, final Rendering<T> rendering) {
            this.jsonType = jsonType;
            this.rendering = rendering;
        }

        static JsonType of(final Class<? extends JsonValue> sourceType) {
            return VALUES.findAny(value -> value.jsonType.isAssignableFrom(sourceType))
                         .orElseThrow(() -> new IllegalStateException(
                                 // Difficult to test ...
                                 "unexpected source type: %s".formatted(sourceType.getCanonicalName())));
        }

        final Renderer render(final Renderer renderer, final JsonValue source, final int indentation) {
            return rendering.render(renderer, source, indentation);
        }
    }

    private interface Rendering<T extends JsonValue> {

        Renderer renderT(Renderer renderer, T source, int indentation);

        @SuppressWarnings("unchecked")
        default Renderer render(final Renderer renderer, final JsonValue source, final int indentation) {
            return renderT(renderer, (T) source, indentation);
        }
    }
}
