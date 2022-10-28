package de.team33.patterns.tuple.janus;

import java.util.Arrays;
import java.util.List;

public class Quad<N, E, S, W> extends Tuple {

    private final N north;
    private final E east;
    private final S south;
    private final W west;

    public Quad(final N north, final E east, final S south, final W west) {
        this.north = north;
        this.east = east;
        this.south = south;
        this.west = west;
    }

    public static <L, R, S, W> Quad<L, R, S, W> of(final L north, final R east, final S south, final W west) {
        return new Quad<>(north, east, south, west);
    }

    public final N north() {
        return north;
    }

    public final E east() {
        return east;
    }

    public final S south() {
        return south;
    }

    public final W west() {
        return west;
    }

    @Override
    public final List<Object> toList() {
        return Arrays.asList(north, east, south, west);
    }
}
