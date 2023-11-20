package de.team33.patterns.decision.telesto.sample;

import java.util.function.Predicate;

public class SamplePlain {

    private static final Y Y0 = new Y();

    // Type of input parameter for criterion k1 ...
    public static class X {
    }

    // Type of input parameter for criterion k2 ...
    public static class Y {
    }

    // Type of input parameter for criterion k3 ...
    public static class Z {
    }

    public enum Result {

        A, B, C, D, E;

        // Criterion 1 ...
        private static final Predicate<X> k1 = x -> x.toString().isEmpty();
        // Criterion 2 ...
        private static final Predicate<Y> k2 = y -> y.equals(Y0);

        // Criterion 3 ...
        private static final Predicate<Z> k3 = z -> z.toString().equals("abc");

        public static Result map(final X x, final Y y, final Z z) {
            if (k1.test(x)) {
                if (k2.test(y)) {
                    if (k3.test(z)) {
                        return A;
                    } else {
                        return C;
                    }
                } else {
                    if (k3.test(z)) {
                        return B;
                    } else {
                        return E;
                    }
                }
            } else {
                if (k2.test(y)) {
                    if (k3.test(z)) {
                        return D;
                    } else {
                        return A;
                    }
                } else {
                    if (k3.test(z)) {
                        return D;
                    } else {
                        return B;
                    }
                }
            }
        }
    }
}
