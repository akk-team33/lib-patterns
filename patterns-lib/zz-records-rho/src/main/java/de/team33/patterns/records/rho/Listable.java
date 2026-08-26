package de.team33.patterns.records.rho;

import de.team33.patterns.typing.proteus.Type;

final class Listable {

    private Listable() {}

    static boolean supports(final Type<?> type) {
        return false;
    }
}
