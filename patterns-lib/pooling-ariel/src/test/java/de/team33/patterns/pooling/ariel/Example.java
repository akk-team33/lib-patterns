package de.team33.patterns.pooling.ariel;

import de.team33.patterns.pooling.ariel.Provider;

import java.util.Random;

public class Example {

    private static final Provider<Random> RANDOM = new Provider<>(Random::new);

    public final void apply() {
        final int anyInt = RANDOM.get(Random::nextInt);
        System.out.append("anyInt = ")
                  .println(anyInt);
    }
}
