package de.team33.patterns.pooling.e1;

import java.util.Random;

public class Example {

    private static final Provider<Random> RANDOM = new Provider<>(Random::new);

    public void apply() {
        final int anyInt = RANDOM.get(Random::nextInt);
        System.out.append("anyInt = ")
                  .println(anyInt);
    }
}
