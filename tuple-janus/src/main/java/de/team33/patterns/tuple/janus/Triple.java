package de.team33.patterns.tuple.janus;

import java.util.Arrays;
import java.util.List;

public class Triple<R, G, B> extends Listable {

    private final R red;
    private final G green;
    private final B blue;

    public Triple(final R red, final G green, final B blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public static <R, G, B> Triple<R, G, B> of(final R red, final G green, final B blue) {
        return new Triple<>(red, green, blue);
    }

    public final R getRed() {
        return red;
    }

    public final G getGreen() {
        return green;
    }

    public final B getBlue() {
        return blue;
    }

    public final List<Object> toList() {
        return Arrays.asList(red, green, blue);
    }
}
