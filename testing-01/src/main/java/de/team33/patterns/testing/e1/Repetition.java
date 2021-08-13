package de.team33.patterns.testing.e1;

public class Repetition {

    public static Report run(final int invocationCount, final int threadCount, final Method method) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    public static final class Report {}

    public static final class Context {

        public final int invocationIndex;

        private Context(final int invocationIndex) {
            this.invocationIndex = invocationIndex;
        }
    }

    @FunctionalInterface
    public interface Method {

        void invoke(Context ctx) throws Exception;
    }
}
