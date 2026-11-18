package de.team33.patterns.records.triton;

import java.math.BigDecimal;
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

    private JsonArray parseArray() {
        source.expect('[')
              .skipWhitespace();
        final JsonArray.Builder builder = JsonArray.builder();
        if (source.hasMore() && ']' == source.peek()) {
            source.skip();
        } else {
            parseArrayBody(builder);
            source.expect(']');
        }
        return builder.build();
    }

    private void parseArrayBody(final JsonArray.Builder builder) {
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

    private JsonString parseString() {
        return new JsonString(StringLiteral.parse(source));
    }

    private void parseMember(final JsonObject.Builder builder) {
        final JsonValue next = parseValue();
        if (next instanceof final JsonString jsonString) {
            final String name = jsonString.value();
            source.expect(COLON)
                  .skipWhitespace();
            final JsonValue value = parseValue();
            builder.put(name, value);
        } else {
            throw new IllegalArgumentException(
                    "expected a value of type %s - but was %s".formatted(JsonString.class.getSimpleName(),
                                                                         next.getClass().getSimpleName()));
        }
    }

    @SuppressWarnings("SameReturnValue")
    private JsonValue parseNull() {
        final String candidate = source.peekUntil(Parser::isLimitChar);
        if (NULL.matcher(candidate).matches()) {
            source.skip(candidate.length());
            return JsonValue.NULL;
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
