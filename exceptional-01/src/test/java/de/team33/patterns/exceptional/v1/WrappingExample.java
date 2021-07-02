package de.team33.patterns.exceptional.v1;

public class WrappingExample {

    public static final Converter WRAPPING1 = Converter.using(Wrapping.method(IllegalStateException::new));
}
