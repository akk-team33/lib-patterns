package de.team33.test.java.util;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RandomTrial {

    private final Random random = new Random();

    @Test
    final void nextInt() {
        assertEquals(0, random.nextInt(1));
    }
}
