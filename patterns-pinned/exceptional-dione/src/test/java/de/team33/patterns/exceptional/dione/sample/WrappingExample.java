package de.team33.patterns.exceptional.dione.sample;

import de.team33.patterns.exceptional.dione.Converter;
import de.team33.patterns.exceptional.dione.Wrapping;

@SuppressWarnings("unused")
public final class WrappingExample {

    public static final Converter WRAPPING = Converter.using(Wrapping.method(IllegalStateException::new));

    private WrappingExample() {
    }
}
