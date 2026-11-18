package de.team33.patterns.records.triton;

import de.team33.patterns.enums.pan.Values;

import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static de.team33.patterns.records.triton.RenderOption.*;
import static java.util.stream.Collectors.joining;

final class Renderer {

    private static final String NEWLINE = "%n".formatted();

    private final StringBuilder target;
    private final Predicate<JsonObject.Entry> entryFilter;
    private final Separation arrSeparation;
    private final Separation objSeparation;

    private Renderer(final Set<RenderOption> options) {
        this.target = new StringBuilder();
        this.entryFilter = options.contains(SKIP_NULL) ? entry -> JsonValue.NULL != entry.value()
                                                       : entry -> true;
        this.objSeparation = options.contains(INLINE_OBJECT) ? new InlineSeparation()
                                                             : new FormattedSeparation();
        this.arrSeparation = options.contains(FORMAT_ARRAY) ? new FormattedSeparation()
                                                            : new InlineSeparation();
    }

    static String render(final JsonValue source, final Set<RenderOption> options) {
        return new Renderer(options).add(source, 0)
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
        return add("{").addObjectBody(source, objSeparation.next(indentation)).add("}");
    }

    private Renderer addObjectBody(final JsonObject source, final int indentation) {
        final List<JsonObject.Entry> entries = source.stream()
                                                     .filter(entryFilter)
                                                     .toList();
        final int size = entries.size();
        if (0 < size) {
            for (int index = 0; index < size; index++) {
                final JsonObject.Entry entry = entries.get(index);
                objSeparation.addSeparator(index, indentation);
                add(StringLiteral.render(entry.name())).add(" : ").add(entry.value(), indentation);
            }
            return objSeparation.addSeparator(0, objSeparation.prev(indentation));
        } else {
            return this;
        }
    }

    private Renderer addArray(final JsonArray source, final int indentation) {
        return add("[").addArrayBody(source, arrSeparation.next(indentation)).add("]");
    }

    private Renderer addArrayBody(final JsonArray source, final int indentation) {
        final int size = source.size();
        if (0 < size) {
            for (int index = 0; index < source.size(); ++index) {
                arrSeparation.addSeparator(index, indentation);
                add(source.get(index), indentation);
            }
            return arrSeparation.addSeparator(0, arrSeparation.prev(indentation));
        } else {
            return this;
        }
    }

    private Renderer addInlineSeparator(final int index) {
        if (0 < index) {
            add(", ");
        }
        return this;
    }

    private Renderer addFormattedSeparator(final int index, final int indentation) {
        if (0 < index) {
            add(",");
        }
        return addNewLine(indentation);
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
                                 // Difficult to test (should not happen at all) ...
                                 "unexpected source type: %s".formatted(sourceType.getCanonicalName())));
        }

        final Renderer render(final Renderer renderer, final JsonValue source, final int indentation) {
            return rendering.render(renderer, source, indentation);
        }
    }

    @FunctionalInterface
    private interface Rendering<T extends JsonValue> {

        Renderer renderT(Renderer renderer, T source, int indentation);

        @SuppressWarnings("unchecked")
        default Renderer render(final Renderer renderer, final JsonValue source, final int indentation) {
            return renderT(renderer, (T) source, indentation);
        }
    }

    private interface Separation {

        int next(int indentation);

        int prev(int indentation);

        Renderer addSeparator(int index, int indentation);
    }

    private class InlineSeparation implements Separation {

        @Override
        public final int next(final int indentation) {
            return indentation;
        }

        @Override
        public final int prev(final int indentation) {
            return indentation;
        }

        @Override
        public final Renderer addSeparator(final int index, final int indentation) {
            return addInlineSeparator(index);
        }
    }

    private class FormattedSeparation implements Separation {

        @Override
        public final int next(final int indentation) {
            return indentation + 1;
        }

        @Override
        public final int prev(final int indentation) {
            return indentation - 1;
        }

        @Override
        public final Renderer addSeparator(final int index, final int indentation) {
            return addFormattedSeparator(index, indentation);
        }
    }
}
