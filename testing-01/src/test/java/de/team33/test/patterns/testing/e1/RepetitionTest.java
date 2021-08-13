package de.team33.test.patterns.testing.e1;

import de.team33.patterns.testing.e1.Repetition;
import org.junit.Test;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class RepetitionTest {

    private static final int PRIME_23 = 23;
    private static final int PRIME_101 = 101;

    @Test
    public final void simpleSuccess() {
        final List<Integer> indices = Collections.synchronizedList(new LinkedList<>());
        Repetition.run(PRIME_101, PRIME_23, ctx -> {
            indices.add(ctx.invocationIndex);
        });

        assertEquals("In total, " + PRIME_101 + " invocation indices must have been collected.",
                     PRIME_101, indices.size());
        final List<Integer> sorted = indices.stream().sorted().distinct().collect(toList());
        assertEquals("Each invocation index must be unique.",
                     sorted.size(), indices.size());
        assertNotEquals("The invocations should NOT have taken place in their original order",
                        sorted, indices);
    }
}
