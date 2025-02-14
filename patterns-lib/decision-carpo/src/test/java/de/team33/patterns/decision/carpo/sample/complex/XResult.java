package de.team33.patterns.decision.carpo.sample.complex;

import de.team33.patterns.decision.carpo.Choices;
import de.team33.patterns.decision.carpo.IntVariety;
import de.team33.patterns.decision.carpo.Variety;
import de.team33.patterns.decision.carpo.sample.Input;

public enum XResult {

    A, B, C, D, E;

    private static final Variety<Input, XResult> VARIETY0 =
            Choices.start(IntVariety.joined(Input::isConditionThree, Input::isConditionTwo, Input::isConditionOne))
                   .on(0b000, 0b101).reply(A)
                   .on(0b001, 0b110).reply(B)
                   .on(0b010).reply(C)
                   .on(0b011).reply(D)
                   .on(0b100).reply(E)
                   .on(0b111).reply(C)
                   .variety();
    private static final Variety<Input, XResult> VARIETY1 =
            Choices.start(IntVariety.joined(Input::isConditionThree, Input::isConditionTwo, Input::isConditionOne))
                    .on(0b000, 0b101).apply(VARIETY0::apply)
                    .on(0b001, 0b110).apply(VARIETY0::apply)
                    .on(0b010).reply(C)
                    .on(0b011).reply(D)
                    .on(0b100).apply(VARIETY0::apply)
                    .on(0b111).reply(C)
                    .variety();

    public static XResult map(final Input input) {
        return VARIETY1.apply(input);
    }
}
