package de.team33.patterns.testing.v1.activity;

public class Swarm {

    public static Report runParallel(final int count, final Operation operation) {
        return run(count, 1, operation);
    }

    public static Report run(final int threadCount, final int loopCount, final Operation operation) {
        return builder().add(threadCount, loopCount, operation).build().run();
        throw new UnsupportedOperationException("not yet implemented");
    }

    public static class Builder {}

    public static class Report {}

    @FunctionalInterface
    public interface Operation {

        Object operate(int threadIndex, int loopIndex) throws Exception;
    }
}
