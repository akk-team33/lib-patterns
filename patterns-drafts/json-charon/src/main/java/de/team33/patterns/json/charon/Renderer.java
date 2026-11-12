package de.team33.patterns.json.charon;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static de.team33.patterns.json.charon.RenderOption.*;
import static java.util.stream.Collectors.joining;

final class Renderer {

    private static final String NEWLINE = "%n".formatted();

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

    Renderer render(final JsonValue source, final int indentation) {
        return switch (source.type()) {
            case NULL, BOOLEAN, NUMBER -> append(source.toString());
            case STRING -> render(source.asString());
            case ARRAY -> render(source.asArray(), indentation);
            case OBJECT -> render(source.asObject(), indentation);
        };
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
