package de.team33.patterns.tuple.janus;

import java.util.Arrays;
import java.util.List;

public class Trip<R, G, B> extends Tuple {

    private final R red;
    private final G green;
    private final B blue;

    public Trip(final R red, final G green, final B blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public static <R, G, B> Trip<R, G, B> of(final R red, final G green, final B blue) {
        return new Trip<>(red, green, blue);
    }

    public final R red() {
        return red;
    }

    public final G green() {
        return green;
    }

    public final B blue() {
        return blue;
    }

    public final List<Object> toList() {
        return Arrays.asList(red, green, blue);
    }
}
