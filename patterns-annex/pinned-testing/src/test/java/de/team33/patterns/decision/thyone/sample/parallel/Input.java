package de.team33.patterns.decision.thyone.sample.parallel;

import java.util.Objects;

public record Input(Object red, Object green, Object blue) {

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
