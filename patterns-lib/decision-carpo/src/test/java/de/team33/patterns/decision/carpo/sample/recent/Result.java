package de.team33.patterns.decision.carpo.sample.recent;

import de.team33.patterns.decision.carpo.Variety;
import de.team33.patterns.decision.carpo.testing.Input;

import java.util.function.Function;

public enum Result {

    A, B, C, D, E;

    private static final Function<Input, Result> FUNCTION = Variety.joined(Input::isC, Input::isB, Input::isA)
                                                                   .on(0b000, 0b001).reply(A)
                                                                   .on(0b010).reply(B)
                                                                   .on(0b011).reply(C)
                                                                   .on(0b100, 0b101, 0b110).reply(D)
                                                                   .on(0b111).reply(E)
                                                                   .toFunction();

    public static Result of(final Input input) {
        return FUNCTION.apply(input);
    }
}
