package de.team33.patterns.exceptional.dione.sample;

import de.team33.patterns.exceptional.dione.Converter;
import de.team33.patterns.exceptional.dione.Wrapping;

public class WrappingExample {

    public static final Converter WRAPPING1 = Converter.using(Wrapping.method(IllegalStateException::new));
}
