package de.team33.patterns.testing.titan;

/**
 * Summarizes various indices that may be of interest in connection with the execution of an operation.
 */
public class Indices {

    /**
     * The index of the executing thread in the order of its instantiation.
     */
    public final int threadIndex;

    /**
     * The index of the executing thread in order of the beginning of its effective execution.
     */
    public final int executionIndex;

    /**
     * The index of the operation in the order of the beginning of its effective execution.
     */
    public final int operationIndex;

    public Indices(final int threadIndex, final int executionIndex, final int operationIndex) {
        this.threadIndex = threadIndex;
        this.executionIndex = executionIndex;
        this.operationIndex = operationIndex;
    }
}
