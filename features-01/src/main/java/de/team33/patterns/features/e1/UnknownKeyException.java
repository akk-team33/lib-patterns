package de.team33.patterns.features.e1;

public class UnknownKeyException extends IllegalArgumentException {

    UnknownKeyException(final Features.Key<?> key) {
        super("Unknown Key: " + key.name() + " -> no Feature specified");
    }
}
