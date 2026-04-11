package de.team33.patterns.decision.thyone.sample.parallel;

import java.util.Objects;

public class Input {

    private final Object red;
    private final Object green;
    private final Object blue;

    public Input(Object red, Object green, Object blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public final Object red() {
        return red;
    }

    public final Object green() {
        return green;
    }

    public final Object blue() {
        return blue;
    }

    public final boolean isRed() {
        return Objects.nonNull(red);
    }

    public final boolean isGreen() {
        return Objects.nonNull(green);
    }

    public final boolean isBlue() {
        return Objects.nonNull(blue);
    }
}
