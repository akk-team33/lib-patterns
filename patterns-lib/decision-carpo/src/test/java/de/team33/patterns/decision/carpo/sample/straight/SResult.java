package de.team33.patterns.decision.carpo.sample.straight;


import de.team33.patterns.decision.carpo.testing.Input;

public enum SResult {

    A, B, C, D, E;

    public static SResult map(final Input input) {
        if (input.isC()) {
            if (input.isB()) {
                if (input.isA()) {
                    return C; // 111 // 7
                } else {
                    return D; // 011 // 3
                }
            } else {
                if (input.isA()) {
                    return A; // 101 // 5
                } else {
                    return B; // 001 // 1
                }
            }
        } else {
            if (input.isB()) {
                if (input.isA()) {
                    return B; // 110 // 6
                } else {
                    return C; // 010 // 2
                }
            } else {
                if (input.isA()) {
                    return E; // 100 // 4
                } else {
                    return A; // 000 // 0
                }
            }
        }
    }
}
