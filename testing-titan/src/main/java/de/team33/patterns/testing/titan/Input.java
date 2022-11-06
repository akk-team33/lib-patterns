package de.team33.patterns.testing.titan;

/**
 * Summarizes various input parameters that may be of interest in the context of the execution of an {@link Operation}.
 *
 * @see Parallel#report(int, Operation)
 * @see Parallel#report(int, int, Operation)
 * @see Parallel#stream(int, Operation)
 * @see Parallel#stream(int, int, Operation)
 */
public class Input {

    /**
     * The index of the executing thread in the order of its instantiation.
     */
    public final int threadIndex;

    /**
     * The index of the executing thread in order of the effective beginning of its execution.
     */
    public final int executionIndex;

    /**
     * The index of the operation in the order of the effective beginning of its execution.
     */
    public final int operationIndex;

    /**
     * The index of the operation loop within one execution;
     */
    public final int loopIndex;

    public Input(final int threadIndex, final int executionIndex, final int operationIndex, final int loopIndex) {
        this.threadIndex = threadIndex;
        this.executionIndex = executionIndex;
        this.operationIndex = operationIndex;
        this.loopIndex = loopIndex;
    }
}
