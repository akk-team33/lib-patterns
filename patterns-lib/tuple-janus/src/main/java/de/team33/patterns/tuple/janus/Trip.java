package de.team33.patterns.tuple.janus;

import java.util.Arrays;
import java.util.List;

/**
 * @deprecated appears largely useless.
 */
@Deprecated
public class Trip<R, G, B> extends Tuple {

    private final R red;
    private final G green;
    private final B blue;

    /**
     * @deprecated see {@link Trip}.
     */
    @Deprecated
    public Trip(final R red, final G green, final B blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    /**
     * @deprecated see {@link Trip}.
     */
    @Deprecated
    public static <R, G, B> Trip<R, G, B> of(final R red, final G green, final B blue) {
        return new Trip<>(red, green, blue);
    }

    /**
     * @deprecated see {@link Trip}.
     */
    @Deprecated
    public final R red() {
        return red;
    }

    /**
     * @deprecated see {@link Trip}.
     */
    @Deprecated
    public final G green() {
        return green;
    }

    /**
     * @deprecated see {@link Trip}.
     */
    @Deprecated
    public final B blue() {
        return blue;
    }

    /**
     * @deprecated see {@link Trip}.
     */
    @Deprecated
    @Override
    public final List<Object> toList() {
        return Arrays.asList(red, green, blue);
    }
}
