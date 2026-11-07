package de.team33.patterns.json.charon;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
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

    private Parser(final String source) {
        this.source = new Source(source);
    }

    static JsonValue parse(final String source) {
        return new Parser(source).parseRoot();
    }

    private static boolean isLimitChar(final char c) {
        return Character.isWhitespace(c) || (0 <= LIMIT_CHARS.indexOf(c));
    }

    private JsonValue parseRoot() {
        source.skipWhitespace();
        final JsonValue result = parseValue();
        source.failIfMore();
        return result;
    }

    private JsonValue parseValue() {
        final char next = source.peek();
        final JsonValue result = switch (next) {
            case '{' -> parseObject();
            case '[' -> parseArray();
            case '"' -> parseString();
            case 'n' -> parseNull();
            case 't', 'f' -> parseBoolean();
            default -> parseNumber();
        };
        source.skipWhitespace();
        return result;
    }

    private JsonObject parseObject() {
        source.expect('{')
              .skipWhitespace();
        final JsonObject.Builder builder = JsonObject.builder();
        if (source.hasMore() && '}' == source.peek()) {
            source.skip();
        } else {
            parseObjectBody(builder);
            source.expect('}');
        }
        return builder.build();
    }

    private void parseObjectBody(final JsonObject.Builder builder) {
        char next = source.hasMore() ? COMMA : 0;
        while (COMMA == next) {
            parseMember(builder);
            next = source.peek();
            if (COMMA == next) {
                source.skip()
                      .skipWhitespace();
            }
        }
    }

    private JsonValue parseArray() {
        source.expect('[')
              .skipWhitespace();
        final List<JsonValue> builder = new ArrayList<>();
        if (source.hasMore() && ']' == source.peek()) {
            source.skip();
        } else {
            parseArrayBody(builder);
            source.expect(']');
        }
        return Json.valueOf(builder);
    }

    private void parseArrayBody(final List<? super JsonValue> builder) {
        char next = source.hasMore() ? COMMA : 0;
        while (COMMA == next) {
            builder.add(parseValue());
            next = source.peek();
            if (COMMA == next) {
                source.skip()
                      .skipWhitespace();
            }
        }
    }

    private JsonValue parseString() {
        return Json.valueOf(StringLiteral.parse(source));
    }

    private void parseMember(final JsonObject.Builder builder) {
        final JsonValue next = parseValue();
        if (JsonType.STRING == next.type()) {
            final String name = next.asString();
            source.expect(COLON)
                  .skipWhitespace();
            final JsonValue value = parseValue();
            builder.put(name, value);
        } else {
            throw new IllegalArgumentException(
                    "expected a value of type %s - but was %s".formatted(JsonType.STRING, next.type()));
        }
    }

    @SuppressWarnings("SameReturnValue")
    private JsonValue parseNull() {
        final String candidate = source.peekUntil(Parser::isLimitChar);
        if (NULL.matcher(candidate).matches()) {
            source.skip(candidate.length());
            return Json.NULL;
        }
        throw new IllegalArgumentException(
                "expected null - but was %s".formatted(candidate));
    }

    private JsonValue parseBoolean() {
        final String candidate = source.peekUntil(Parser::isLimitChar);
        if (BOOLEAN.matcher(candidate).matches()) {
            source.skip(candidate.length());
            return Json.valueOf("true".equals(candidate));
        }
        throw new IllegalArgumentException("expected one of {true, false} - but was %s".formatted(candidate));
    }

    private JsonValue parseNumber() {
        final String candidate = source.peekUntil(Parser::isLimitChar);
        if (NUMBER.matcher(candidate).matches()) {
            final BigDecimal number = new BigDecimal(candidate);
            source.skip(candidate.length());
            return Json.valueOf(number);
        }
        throw new IllegalArgumentException("expected Json number - but was %s".formatted(candidate));
    }
}
