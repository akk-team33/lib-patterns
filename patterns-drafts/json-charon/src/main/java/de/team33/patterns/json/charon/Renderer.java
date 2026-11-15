package de.team33.patterns.json.charon;

import de.team33.patterns.collection.mneme.FinalEntry;
import de.team33.patterns.collection.mneme.FinalMap;
import de.team33.patterns.collection.mneme.ImmutableMap;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static de.team33.patterns.json.charon.RenderOption.*;
import static java.util.stream.Collectors.joining;

final class Renderer {

    private static final String NEWLINE = "%n".formatted();
    private static final ImmutableMap<Class<? extends JsonValue>, Rendering<?>> RENDERINGS =
            Stream.of(renderEntry(JsonNull.class, Renderer::renderPlain),
                      renderEntry(JsonBoolean.class, Renderer::renderPlain),
                      renderEntry(JsonNumber.class, Renderer::renderPlain),
                      renderEntry(JsonString.class, Renderer::renderString),
                      renderEntry(JsonArray.class, Renderer::renderArray),
                      renderEntry(JsonObject.class, Renderer::renderObject))
                  .collect(FinalMap.collector());

    private final StringBuilder target;
    private final Predicate<Map.Entry<?, ?>> entryFilter;
    private final Separation arrSeparation;
    private final Separation objSeparation;

    Renderer(final Set<RenderOption> options) {
        this.target = new StringBuilder();
        this.entryFilter = options.contains(SKIP_NULL) ? entry -> Json.NULL != entry.getValue()
                                                       : entry -> true;
        this.objSeparation = options.contains(INLINE_OBJECT) ? new InlineSeparation()
                                                             : new FormattedSeparation();
        this.arrSeparation = options.contains(FORMAT_ARRAY) ? new FormattedSeparation()
                                                            : new InlineSeparation();
    }

    private static <J extends JsonValue>
    Map.Entry<Class<J>, Rendering<J>> renderEntry(final Class<J> jClass, final Rendering<J> rendering) {
        return FinalEntry.of(jClass, rendering);
    }

    private Renderer append(final String source) {
        target.append(source);
        return this;
    }

    private Renderer addNewLine(final int indentation) {
        return append(NEWLINE).addSpace(indentation);
    }

    private Renderer addSpace(final int indentation) {
        return append(Stream.generate(() -> "   ")
                            .limit(indentation)
                            .collect(joining()));
    }

    private static Rendering<?> rendering(final Class<? extends JsonValue> type) {
        return Optional.ofNullable(RENDERINGS.get(type))
                       .orElseThrow(() -> new IllegalArgumentException("not supported: " + type));
    }

    Renderer render(final JsonValue source, final int indentation) {
        return rendering(source.getClass()).render(this, source, indentation);
    }

    private Renderer renderObject(final JsonObject value, final int indentation) {
        return append("{").renderBody(value.value(), objSeparation.next(indentation)).append("}");
    }

    private Renderer renderArray(final JsonArray value, final int indentation) {
        return append("[").renderBody(value.value(), arrSeparation.next(indentation)).append("]");
    }

    private Renderer renderString(final JsonString value, final int indentation) {
        return append(StringLiteral.render(value.value()));
    }

    private Renderer renderPlain(final JsonValue value, final int indentation) {
        return append(value.toString());
    }

    @Override
    public final String toString() {
        return target.toString();
    }

    private Renderer render(final Map<String, JsonValue> source, final int indentation) {
        return append("{").renderBody(source, objSeparation.next(indentation)).append("}");
    }

    private Renderer renderBody(final Map<String, JsonValue> source, final int indentation) {
        final List<Map.Entry<String, JsonValue>> entries = source.entrySet().stream()
                                                                 .filter(entryFilter)
                                                                 .toList();
        final int size = entries.size();
        if (0 < size) {
            for (int index = 0; index < size; index++) {
                final Map.Entry<String, JsonValue> entry = entries.get(index);
                objSeparation.addSeparator(index, indentation);
                render(entry.getKey()).append(" : ").render(entry.getValue(), indentation);
            }
            return objSeparation.addSeparator(0, objSeparation.prev(indentation));
        } else {
            return this;
        }
    }

    private Renderer render(final List<? extends JsonValue> source, final int indentation) {
        return append("[").renderBody(source, arrSeparation.next(indentation)).append("]");
    }

    private Renderer renderBody(final List<? extends JsonValue> source, final int indentation) {
        final int size = source.size();
        if (0 < size) {
            for (int index = 0; index < source.size(); ++index) {
                arrSeparation.addSeparator(index, indentation);
                render(source.get(index), indentation);
            }
            return arrSeparation.addSeparator(0, arrSeparation.prev(indentation));
        } else {
            return this;
        }
    }

    private Renderer render(final String source) {
        return append(StringLiteral.render(source));
    }

    @FunctionalInterface
    private interface Rendering<J extends JsonValue> {

        Renderer renderJ(Renderer renderer, J jsonValue, int indentation);

        @SuppressWarnings("unchecked")
        default Renderer render(final Renderer renderer, final JsonValue jsonValue, final int indentation) {
            return renderJ(renderer, (J) jsonValue, indentation);
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
            if (0 < index) {
                append(", ");
            }
            return Renderer.this;
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
            if (0 < index) {
                append(",");
            }
            return addNewLine(indentation);
        }
    }
}
