package de.team33.patterns.json.charon;

import de.team33.patterns.streamable.galatea.Buffer;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

final class Parser {

    private static final String LIMIT_CHARS = ",}]";
    private static final Pattern NUMBER =
            Pattern.compile("-?(0|[1-9][0-9]*)(\\.[0-9]+)?([eE][+-]?[0-9]+)?");
    private static final Pattern NULL = Pattern.compile("null");
    private static final Pattern BOOLEAN = Pattern.compile("true|false");
    private static final char COMMA = ',';
    private static final char COLON = ':';

    private final Source source;

    Parser(final String source) {
        this.source = new Source(source);
    }

    private static boolean isLimitChar(final char c) {
        return Character.isWhitespace(c) || (0 <= LIMIT_CHARS.indexOf(c));
    }

    Json.Value parseRoot() {
        source.skipWhitespace();
        final Json.Value result = parseItem();
        source.failIfMore();
        return result;
    }

    private Json.Value parseItem() {
        final Json.Value result = parseItem(source.peek());
        source.skipWhitespace();
        return result;
    }

    private Json.Value parseItem(final char next) {
        return switch (next) {
            case '{' -> parseObject();
            case '[' -> parseArray();
            case '"' -> parseString();
            case 'n' -> parseNull();
            case 't', 'f' -> parseBoolean();
            default -> parseNumber();
        };
    }

    private JsonObject parseObject() {
        source.expect('{')
              .skipWhitespace();
        final Map<String, Json.Value> stage = new LinkedHashMap<>();
        if (source.hasMore() && '}' == source.peek()) {
            source.skip();
        } else {
            parseObjectBody(stage);
            source.expect('}');
        }
        return new JsonObject(stage);
    }

    private void parseObjectBody(final Map<? super String, ? super Json.Value> stage) {
        char next = source.hasMore() ? COMMA : 0;
        while (COMMA == next) {
            parseMember(stage);
            next = source.peek();
            if (COMMA == next) {
                source.skip()
                      .skipWhitespace();
            }
        }
    }

    private void parseMember(final Map<? super String, ? super Json.Value> stage) {
        final Json.Value next = parseItem();
        if (next instanceof final JsonString jsonString) {
            final String name = jsonString.value();
            source.expect(COLON)
                  .skipWhitespace();
            final Json.Value value = parseItem();
            stage.put(name, value);
        } else {
            throw new IllegalArgumentException(
                    "expected a value of type %s - but was %s".formatted(JsonString.class.getSimpleName(),
                                                                         next.getClass().getSimpleName()));
        }
    }

    private JsonArray parseArray() {
        source.expect('[').skipWhitespace();
        final Buffer<Json.Value> buffer = Buffer.empty();
        if (source.hasMore() && ']' == source.peek()) {
            source.skip();
        } else {
            parseArrayBody(buffer);
            source.expect(']');
        }
        return new JsonArray(buffer);
    }

    private void parseArrayBody(final Buffer<? super Json.Value> buffer) {
        char next = source.hasMore() ? COMMA : 0;
        while (COMMA == next) {
            buffer.add(parseItem());
            next = source.peek();
            if (COMMA == next) {
                source.skip()
                      .skipWhitespace();
            }
        }
    }

    private JsonString parseString() {
        return new JsonString(StringLiteral.parse(source));
    }

    @SuppressWarnings("SameReturnValue")
    private Json.Value parseNull() {
        final String candidate = source.peekUntil(Parser::isLimitChar);
        if (NULL.matcher(candidate).matches()) {
            source.skip(candidate.length());
            return Json.NULL;
        }
        throw new IllegalArgumentException(
                "expected null - but was %s".formatted(candidate));
    }

    private JsonBoolean parseBoolean() {
        final String candidate = source.peekUntil(Parser::isLimitChar);
        if (BOOLEAN.matcher(candidate).matches()) {
            source.skip(candidate.length());
            return new JsonBoolean("true".equals(candidate));
        }
        throw new IllegalArgumentException("expected one of {true, false} - but was %s".formatted(candidate));
    }

    private JsonNumber parseNumber() {
        final String candidate = source.peekUntil(Parser::isLimitChar);
        if (NUMBER.matcher(candidate).matches()) {
            final BigDecimal number = new BigDecimal(candidate);
            source.skip(candidate.length());
            return new JsonNumber(number);
        }
        throw new IllegalArgumentException("expected Json number - but was %s".formatted(candidate));
    }
}
