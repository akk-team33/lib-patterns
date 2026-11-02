package de.team33.patterns.json.sinope;

import de.team33.patterns.typing.proteus.Type;

import java.math.BigDecimal;

class Numerable {

    static <T> T decode(final Type<T> type, final BigDecimal normal) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    static <T> BigDecimal encode(final Type<T> type, final T value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    static boolean supports(final Type<?> type) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
