package de.team33.patterns.decision.telesto.sample;

public class SamplePlain {

    public enum Result {

        A, B, C, D, E;

        public static Result map(final X x, final Y y, final Z z) {
            if (x.k1()) {
                if (y.k2()) {
                    if (z.k3()) {
                        return A;
                    } else {
                        return C;
                    }
                } else {
                    if (z.k3()) {
                        return B;
                    } else {
                        return E;
                    }
                }
            } else {
                if (y.k2()) {
                    if (z.k3()) {
                        return D;
                    } else {
                        return A;
                    }
                } else {
                    if (z.k3()) {
                        return D;
                    } else {
                        return B;
                    }
                }
            }
        }
    }
}
