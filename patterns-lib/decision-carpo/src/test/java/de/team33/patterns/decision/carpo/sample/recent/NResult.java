package de.team33.patterns.decision.carpo.sample.recent;

import de.team33.patterns.decision.carpo.Variety;
import de.team33.patterns.decision.carpo.testing.Input;

import java.util.function.Function;

public enum NResult {

    A, B, C, D, E;

    private static final Function<Input, NResult> FUNCTION =
            Variety.joined(Input::isC, Input::isB, Input::isA)
                   .on(0b000, 0b101).reply(A)
                   .on(0b001, 0b110).reply(B)
                   .on(0b010).reply(C)
                   .on(0b100).reply(E)
                   .on(0b111).reply(C)
                   .toFunction();

    public static NResult map(final Input input) {
        return FUNCTION.apply(input);
    }
}
