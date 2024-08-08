package de.team33.patterns.testing.titan;

/**
 * @deprecated use de.team33.testing.async.thebe.Context instead -
 * see <a href="http://www.team33.de/dev/testing/1.x/testing-lib/async-thebe/">de.team33.testing:async-thebe</a>
 */
@Deprecated
public class Context {

    /**
     * The index of the executing thread in the order of its instantiation.
     */
    public final int threadIndex;

    /**
     * The index of the operation in the order of the actual beginning of its execution.
     */
    public final int operationIndex;

    /**
     * The index of the operation loop within one execution;
     */
    public final int loopIndex;

    public Context(final int threadIndex, final int operationIndex, final int loopIndex) {
        this.threadIndex = threadIndex;
        this.operationIndex = operationIndex;
        this.loopIndex = loopIndex;
    }
}
