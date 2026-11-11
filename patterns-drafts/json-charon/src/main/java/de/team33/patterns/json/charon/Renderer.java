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

    private Renderer(final Set<RenderOption> options) {
        this.target = new StringBuilder();
        this.entryFilter = options.contains(SKIP_NULL) ? entry -> Json.NULL != entry.getValue()
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
        return switch (source.type()) {
            case NULL, BOOLEAN, NUMBER -> add(source.toString());
            case STRING -> addString(source);
            case ARRAY -> addArray(source, indentation);
            case OBJECT -> addObject(source, indentation);
        };
    }

    @Override
    public final String toString() {
        return target.toString();
    }

    private Renderer addObject(final JsonValue source, final int indentation) {
        return add("{").addObjectBody(source, objSeparation.next(indentation)).add("}");
    }

    private Renderer addObjectBody(final JsonValue source, final int indentation) {
        final List<Map.Entry<String, JsonValue>> entries = source.asObject().entrySet().stream()
                                                                 .filter(entryFilter)
                                                                 .toList();
        final int size = entries.size();
        if (0 < size) {
            for (int index = 0; index < size; index++) {
                final Map.Entry<String, JsonValue> entry = entries.get(index);
                objSeparation.addSeparator(index, indentation);
                add(StringLiteral.render(entry.getKey())).add(" : ").add(entry.getValue(), indentation);
            }
            return objSeparation.addSeparator(0, objSeparation.prev(indentation));
        } else {
            return this;
        }
    }

    private Renderer addArray(final JsonValue source, final int indentation) {
        return add("[").addArrayBody(source.asArray(), arrSeparation.next(indentation)).add("]");
    }

    private Renderer addArrayBody(final List<? extends JsonValue> source, final int indentation) {
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

    private Renderer addString(final JsonValue source) {
        return add(StringLiteral.render(source.asString()));
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
