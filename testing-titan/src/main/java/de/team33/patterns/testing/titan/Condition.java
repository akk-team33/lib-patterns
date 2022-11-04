package de.team33.patterns.testing.titan;

/**
 * Defines the condition under which {@link Parallel} execution should take place.
 *
 * @see #byThreads(int)
 * @see #byThreads(int, int)
 * @see #byOperations(int, int)
 */
public class Condition {

    private final int numberOfThreads;
    private final int minNumberOfOperations;
    private final int maxNumberOfOperations;

    private Condition(final int numberOfThreads, final int minNumberOfOperations, final int maxNumberOfOperations) {
        this.numberOfThreads = numberOfThreads;
        this.minNumberOfOperations = minNumberOfOperations;
        this.maxNumberOfOperations = maxNumberOfOperations;
    }

    /**
     * Returns a {@link Condition} specifying a number of threads to run in {@link Parallel},
     * such that each thread performs a given {@link Operation} at least once.
     */
    public static Condition byThreads(final int numberOfThreads) {
        return new Condition(numberOfThreads, numberOfThreads, Integer.MAX_VALUE);
    }

    /**
     * Returns a {@link Condition} specifying a specific number of threads to run in {@link Parallel},
     * such that each thread performs a given {@link Operation} at least once.
     * <p>
     * In addition, it is specified how often the {@link Operation} is to be carried out at least in total.
     * This specification only makes sense if it is greater than the number of threads to be executed.
     */
    public static Condition byThreads(final int numberOfThreads, final int minNumberOfOperations) {
        return new Condition(numberOfThreads, minNumberOfOperations, Integer.MAX_VALUE);
    }

    /**
     * Returns a {@link Condition} that specifies the total number of times a given {@link Operation} should be
     * performed in {@link Parallel}.
     * <p>
     * In addition, it is specified how many threads should be started at most in order to carry out the
     * {@link Operation}.
     */
    public static Condition byOperations(final int numberOfOperations, final int maxNumberOfThreads) {
        return new Condition(maxNumberOfThreads, numberOfOperations, numberOfOperations);
    }

    public int getNumberOfThreads() {
        return numberOfThreads;
    }

    public int getMinNumberOfOperations() {
        return minNumberOfOperations;
    }

    public int getMaxNumberOfOperations() {
        return maxNumberOfOperations;
    }
}
