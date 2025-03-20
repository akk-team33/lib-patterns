package de.team33.patterns.decision.carpo.sample.recent;

import de.team33.patterns.decision.carpo.Variety;
import de.team33.patterns.decision.carpo.testing.Input;

@SuppressWarnings("unused")
public enum VResult {

    A, B, C, D, E, F, G, H;

    private static final Variety<Input> VARIETY = Variety.joined(Input::isC, Input::isB, Input::isA);

    public static VResult of(final Input input) {
        return values()[VARIETY.apply(input)];
    }
}
