package de.team33.patterns.json.charon;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static de.team33.patterns.json.charon.Json.Option.*;
import static java.util.stream.Collectors.joining;

final class Renderer {

    private static final String NEWLINE = "%n".formatted();

    private final StringBuilder target;
    private final Predicate<Map.Entry<String, ? extends Json.Value>> entryFilter;
    private final Separation arrSeparation;
    private final Separation objSeparation;

    Renderer(final Set<Json.Option> options) {
        this.target = new StringBuilder();
        this.entryFilter = options.contains(SKIP_NULL) ? entry -> Json.NULL != entry.getValue()
                                                       : entry -> true;
        this.objSeparation = options.contains(INLINE_OBJECT) ? new InlineSeparation()
                                                             : new FormattedSeparation();
        this.arrSeparation = options.contains(FORMAT_ARRAY) ? new FormattedSeparation()
                                                            : new InlineSeparation();
    }

    private Renderer add(final String source) {
        target.append(source);
        return this;
    }

    private Renderer addSpace(final int indentation) {
        return add(Stream.generate(() -> "   ")
                         .limit(indentation)
                         .collect(joining()));
    }

    Renderer render(final Json.Value source, final int indentation) {
        return switch (source.type()) {
            case NULL -> add("null");
            case BOOLEAN -> add(source.asBoolean().toString());
            case NUMBER -> add(source.asBigDecimal().toString());
            case STRING -> add(StringLiteral.render(source.asString()));
            case ARRAY -> addArray(source.asList(), indentation);
            case OBJECT -> addObject(source.asMap(), indentation);
        };
    }

    @Override
    public final String toString() {
        return target.toString();
    }

    private Renderer addObject(final Map<String, Json.Value> source, final int indentation) {
        return add("{").addObjectBody(source, objSeparation.next(indentation)).add("}");
    }

    private Renderer addObjectBody(final Map<String, Json.Value> value, final int indentation) {
        final List<Map.Entry<String, Json.Value>> entries = value.entrySet().stream()
                                                                 .filter(entryFilter)
                                                                 .toList();
        final int size = entries.size();
        if (0 < size) {
            for (int index = 0; index < size; index++) {
                final Map.Entry<String, Json.Value> entry = entries.get(index);
                objSeparation.addSeparator(index, indentation);
                add(StringLiteral.render(entry.getKey())).add(" : ").render(entry.getValue(), indentation);
            }
            return objSeparation.addSeparator(0, objSeparation.prev(indentation));
        } else {
            return this;
        }
    }

    private Renderer addArray(final List<? extends Json.Value> values, final int indentation) {
        return add("[").addArrayBody(values, arrSeparation.next(indentation)).add("]");
    }

    private Renderer addArrayBody(final List<? extends Json.Value> values, final int indentation) {
        final int size = values.size();
        if (0 < size) {
            for (int index = 0; index < size; ++index) {
                arrSeparation.addSeparator(index, indentation);
                render(values.get(index), indentation);
            }
            return arrSeparation.addSeparator(0, arrSeparation.prev(indentation));
        } else {
            return this;
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
                add(", ");
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
                add(",");
            }
            return add(NEWLINE).addSpace(indentation);
        }
    }
}
