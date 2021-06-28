package de.team33.test.patterns.testing.v1.activity;

import de.team33.patterns.testing.v1.activity.Swarm;
import de.team33.patterns.testing.v1.activity.Swarm.Report;
import org.junit.Test;

public class SwarmTest {

    @Test
    public final void runParallel() {
        final Report report = Swarm.runParallel(10, this::operate);
    }

    @Test
    public final void run() {
        final Report report = Swarm.run(10, 10, this::operate);
    }

    private Object operate(final int threadIndex, final int loopIndex) {
        throw new UnsupportedOperationException("not yet implemented");
    }
}
