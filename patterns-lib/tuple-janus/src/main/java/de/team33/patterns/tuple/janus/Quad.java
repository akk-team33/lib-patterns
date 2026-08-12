package de.team33.patterns.tuple.janus;

import java.util.Arrays;
import java.util.List;

/**
 * @deprecated appears largely useless.
 */
@Deprecated
public class Quad<N, E, S, W> extends Tuple {

    private final N north;
    private final E east;
    private final S south;
    private final W west;

    /**
     * @deprecated see {@link Quad}.
     */
    @Deprecated
    public Quad(final N north, final E east, final S south, final W west) {
        this.north = north;
        this.east = east;
        this.south = south;
        this.west = west;
    }

    /**
     * @deprecated see {@link Quad}.
     */
    @Deprecated
    public static <L, R, S, W> Quad<L, R, S, W> of(final L north, final R east, final S south, final W west) {
        return new Quad<>(north, east, south, west);
    }

    /**
     * @deprecated see {@link Quad}.
     */
    @Deprecated
    public final N north() {
        return north;
    }

    /**
     * @deprecated see {@link Quad}.
     */
    @Deprecated
    public final E east() {
        return east;
    }

    /**
     * @deprecated see {@link Quad}.
     */
    @Deprecated
    public final S south() {
        return south;
    }

    /**
     * @deprecated see {@link Quad}.
     */
    @Deprecated
    public final W west() {
        return west;
    }

    /**
     * @deprecated see {@link Quad}.
     */
    @Deprecated
    @Override
    public final List<Object> toList() {
        return Arrays.asList(north, east, south, west);
    }
}
