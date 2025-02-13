package de.team33.patterns.decision.leda.sample.complex;

import de.team33.patterns.decision.leda.XVariety;
import de.team33.patterns.decision.leda.sample.Input;

public enum XResult {

    A, B, C, D, E;

    private static final XVariety<Input, XResult> VARIETY0 =
            XVariety.joining(Input::isConditionThree, Input::isConditionTwo, Input::isConditionOne)
                    .on(0b000, 0b101).reply(A)
                    .on(0b001, 0b110).reply(B)
                    .on(0b010).reply(C)
                    .on(0b011).reply(D)
                    .on(0b100).reply(E)
                    .on(0b111).reply(C)
                    .build();
    private static final XVariety<Input, XResult> VARIETY1 =
            XVariety.joining(Input::isConditionThree, Input::isConditionTwo, Input::isConditionOne)
                    .on(0b000, 0b101).apply(VARIETY0::apply)
                    .on(0b001, 0b110).apply(VARIETY0::apply)
                    .on(0b010).reply(C)
                    .on(0b011).reply(D)
                    .on(0b100).apply(VARIETY0::apply)
                    .on(0b111).reply(C)
                    .build();

    public static XResult map(final Input input) {
        return VARIETY0.apply(input);
    }
}
