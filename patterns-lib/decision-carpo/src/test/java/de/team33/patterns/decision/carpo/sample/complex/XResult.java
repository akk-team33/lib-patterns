package de.team33.patterns.decision.carpo.sample.complex;

import de.team33.patterns.decision.carpo.Variety;
import de.team33.patterns.decision.carpo.sample.Input;

import java.util.function.Function;

public enum XResult {

    A, B, C, D, E;

    private static final Function<Input, XResult> F1 =
            Variety.joined(Input::isConditionThree, Input::isConditionTwo, Input::isConditionOne)
                   .choices()
                   .on(0b000, 0b101).reply(A)
                   .on(0b001, 0b110).reply(B)
                   .on(0b010).reply(C)
                   .on(0b011).reply(D)
                   .on(0b100).reply(E)
                   .on(0b111).reply(C)
                   .toFunction();
    private static final Function<Input, XResult> F2 =
            Variety.joined(Input::isConditionThree, Input::isConditionTwo, Input::isConditionOne)
                   .choices()
                   .on(0b000, 0b101).apply(F1)
                   .on(0b001, 0b110).apply(F1)
                   .on(0b010).reply(C)
                   .on(0b011).reply(D)
                   .on(0b100).apply(F1)
                   .on(0b111).reply(C)
                   .toFunction();

    public static XResult map(final Input input) {
        return F2.apply(input);
    }
}
