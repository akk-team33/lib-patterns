package de.team33.patterns.expiry.tethys.publics;

class Result {

    private final int index;
    private final long delta;

    Result(final int index, final long delta) {
        this.index = index;
        this.delta = delta;
    }

    final int index() {
        return index;
    }

    final long delta() {
        return delta;
    }
}
