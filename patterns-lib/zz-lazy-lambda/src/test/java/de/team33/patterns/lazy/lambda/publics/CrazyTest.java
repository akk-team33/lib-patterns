package de.team33.patterns.lazy.lambda.publics;

import de.team33.patterns.lazy.lambda.Crazy;
import de.team33.testing.async.thebe.Parallel;
import de.team33.testing.bridging.styx.Bridger;
import org.junit.jupiter.api.Test;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CrazyTest {

    private static final Bridger BRIDGER = new Bridger();

    private final Random random = new SecureRandom();
    private final List<Integer> values = Collections.synchronizedList(new ArrayList<>());

    private int initial() {
        final int result = random.nextInt();
        values.add(result);
        BRIDGER.bridge(1); // spend some time
        return result;
    }

    @Test
    final void get_sequential() throws Exception {
        final Crazy<Integer> crazy = Crazy.init(this::initial);
        final List<Integer> results = IntStream.range(0, 100)
                                               .mapToObj(index -> crazy.get())
                                               .toList();
        assertEquals(1, values.size());
        results.forEach(result -> assertEquals(values.get(0), result));
    }

    @Test
    final void get_parallel() throws Exception {
        final Crazy<Integer> crazy = Crazy.init(this::initial);
        final List<Integer> results = Parallel.report(100, ctx -> crazy.get())
                                              .reThrowAny()
                                              .list();
        assertEquals(1, values.size());
        results.forEach(result -> assertEquals(values.get(0), result));
    }
}